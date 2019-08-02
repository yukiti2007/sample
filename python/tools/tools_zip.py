import os
import zipfile


class Zip:

    # 压缩一个文件
    @staticmethod
    def do_zip_file(file_path, file_name, zip_file):
        # 创建压缩包
        with zipfile.ZipFile(zip_file, "a") as write:
            # 往压缩包里添加文件
            write.write(file_path + file_name, file_name)

            # 打印压缩包信息
            write.printdir()
            write.close()

    # 压缩一个文件夹
    @staticmethod
    def do_zip_dir(dir_path, zip_file):
        # 创建压缩包
        with zipfile.ZipFile(zip_file, "a") as write:
            for root, dirs, files in os.walk(dir_path):
                for file in files:
                    # 往压缩包里添加文件
                    file_path = root + "/" + file
                    write.write(file_path, file_path.replace(dir_path, ""))

            # 打印压缩包信息
            write.printdir()
            write.close()

    # 解压一个文件
    @staticmethod
    def do_unzip(zip_file, unzip_dir):
        # 解压压缩包
        with zipfile.ZipFile(zip_file, "r") as read:
            read.extractall(unzip_dir)
            read.close()


if __name__ == "__main__":
    pass
