import threading

from kazoo.client import KazooClient
from kazoo.exceptions import NoNodeError, NodeExistsError, NoChildrenForEphemeralsError, BadVersionError


def _zk_ex_comm_handler(fun):
    def inner(*args, **kwargs):
        try:
            return fun(*args, **kwargs)
        except (NoNodeError, NodeExistsError, NoChildrenForEphemeralsError, BadVersionError, TypeError) as e:
            raise e
            pass
        except BaseException as e:
            LOGGER.error("ZK BaseException")
            ZK.release()

    return inner


class ZK:
    _zk_hosts = []
    _zk_client = None
    _root_path = "/rootpath"
    _lock = threading.Lock()

    @staticmethod
    def _get_inst():
        """
        获取zk客户端实例
        拼装ZK._root_path，并初始化节点
        :return:    zk客户端实例
        """
        try:
            if ZK._zk_client is None:
                ZK._lock.acquire()
                if ZK._zk_client is None:
                    LOGGER.info("#####", "Starting ZK Client On", Tools.get_local_ip(), "#####")
                    hosts = ""
                    for host in ZK._zk_hosts:
                        hosts += host + ","
                    hosts = hosts[:len(hosts) - 1]
                    ZK._zk_client = KazooClient(hosts=hosts)
                    ZK._zk_client.start()
                    LOGGER.info("#####", "ZK Client Started Successfully On", Tools.get_local_ip(), "Root Path:",
                                ZK._root_path, "#####")

                    # 初始化节点
                    if ZK._zk_client.exists(ZK._root_path) is None:
                        LOGGER.info("Creating ZK Root Path:", ZK._root_path, "On", Tools.get_local_ip())
                        ZK._zk_client.create(ZK._root_path, makepath=True)

        except BaseException as e:
            LOGGER.error("#####", "ZK Client Started Failed On", Tools.get_local_ip(), "#####")
        finally:
            if ZK._lock.locked():
                ZK._lock.release()

        return ZK._zk_client

    @staticmethod
    def release():
        """
        关闭zk连接
        :return:
        """
        try:
            if ZK._zk_client is not None:
                LOGGER.info("#####", "Stopping ZK Client On", Tools.get_local_ip(), "#####")
                ZK._zk_client.stop()
            LOGGER.error("#####", "ZK Client Stopped Successfully On", Tools.get_local_ip(), "#####")
        except BaseException:
            LOGGER.error("#####", "ZK Client Stopped Failed On", Tools.get_local_ip(), "#####")
        finally:
            ZK._zk_client = None

    @staticmethod
    def _get_path(path):
        """
        重新组装path,path都必须以ZK._root_path开头
        :param path:
        :return:
        """
        if path.startswith(ZK._root_path):
            return path
        else:
            return ZK._root_path + path

    @staticmethod
    @_zk_ex_comm_handler
    def create(path, value=b"", ephemeral=False, sequence=False):
        """
        创建节点，同时写入数据，创建成功则返回节点路径
        :param path:        节点路径
        :param value:       写入节点的数据
        :param ephemeral:   是否为临时节点
        :param sequence:    是否递归创建节点
        :return:            节点路径
        :exception NodeExistsError: 节点已经存在
        :exception NoChildrenForEphemeralsError:    父节点为临时节点
        """
        inst = ZK._get_inst()
        path = ZK._get_path(path)
        return inst.create(path, value=value, ephemeral=ephemeral, sequence=sequence)

    @staticmethod
    @_zk_ex_comm_handler
    def exists(path, watch=None):
        """
        如果节点存在，返回节点的ZnodeStat信息
        如果节点不存在，返回None
        :param path:    节点路径
        :param watch:   回调方法，当path所指向的节点有数据变更、节点删除时回调
                        回调方法参数WatchedEvent(type, state, path)
                        type:   CREATED 节点被创建
                                DELETED 节点被删除
                                CHANGED 节点数据被修改
                                NONE    连接状态被修改
        :return:        节点的ZnodeStat信息 / None
        """
        inst = ZK._get_inst()
        path = ZK._get_path(path)
        return inst.exists(path, watch=watch)

    @staticmethod
    @_zk_ex_comm_handler
    def get(path, watch=None):
        """
        获取path指向节点的数据及ZnodeStat信息
        :param path:    节点路径
        :param watch:   回调方法，当path所指向的节点有数据变更、节点删除时回调
                        回调方法参数WatchedEvent(type, state, path)
                        type:   DELETED 节点被删除
                                CHANGED 节点数据被修改
                                NONE    连接状态被修改
        :return:        (数据, ZnodeStat信息)
        :exception NoNodeError: path指向的节点不存在
        """
        inst = ZK._get_inst()
        path = ZK._get_path(path)
        return inst.get(path, watch=watch)

    @staticmethod
    @_zk_ex_comm_handler
    def get_children(path, watch=None, include_data=False):
        """
        获取path指向节点的子节点列表，前节点的ZnodeStat信息
        :param path:            节点路径
        :param watch:           回调方法，当path指向的节点，有子节点增减的时候回调该
                                回调方法参数WatchedEvent(type, state, path)
                                type:   CHILD   子节点本创建或删除（子节点数据变更不会回调）
                                        NONE    连接状态被修改
        :param include_data:    是否包含当前节点的ZnodeStat信息
        :return:                [子节点列表] / ([子节点列表], 当前节点的ZnodeStat信息)
        :exception NoNodeError: path指向的节点不存在
        """
        inst = ZK._get_inst()
        path = ZK._get_path(path)
        return inst.get_children(path, watch=watch, include_data=include_data)

    @staticmethod
    @_zk_ex_comm_handler
    def set(path, value=b"", version=-1):
        """
        修改节点上的数据，修改成功则返回节点的ZnodeStat信息
        :param path:            节点路径
        :param value:           新的数据
        :param version:         如果版本号一致则修改，-1:不确认版本号强制修改
        :return:                节点的ZnodeStat信息
        :exception NoNodeError:     path指向的节点不存在
        :exception BadVersionError: 版本号不一致
        """
        inst = ZK._get_inst()
        path = ZK._get_path(path)
        return inst.set(path, value, version=version)

    @staticmethod
    @_zk_ex_comm_handler
    def delete(path, version=-1, recursive=False):
        """
        删除节点，删除成功则返回True
        :param path:            节点路径
        :param version:         如果版本号一致则删除，-1:不确认版本号强制删除
        :param recursive:       是否递归删除
        :return:                True
        :exception NoNodeError:     path指向的节点不存在
        :exception BadVersionError: 版本号不一致
        """
        inst = ZK._get_inst()
        path = ZK._get_path(path)
        return inst.delete(path, version=version, recursive=recursive)


def call_back(event):
    type, state, path = event
    # print(type)
    # print(state)
    # print(path)
    print("call_back :" + str(event))


if __name__ == "__main__":
    print(ZK.delete("/hello"))
    print(ZK.delete("/hello2"))
    print(ZK.exists("/hello", watch=call_back))
    print(ZK.create("/hello", "hellohello".encode("utf8")))
    print(ZK.create("/hello2", "hello2hello2".encode("utf8")))
    # print(ZK.get_children("", watch=p))
    # print(ZK.get_children(""))
    # print(ZK.get("", watch=p))
    # ZK.set("/hello2", "aaa".encode("utf8"))
    # print(ZK.delete("/hello2"))
