from flask import Flask

app = Flask(__name__)


@app.route("/hello1", methods=["GET", "POST"])
def hello_world1():
    return "hello_world1"


def hello_world2():
    return "hello world2"


app.add_url_rule("/hello2", view_func=hello_world2, methods=["GET", "POST"])

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080, debug=True)
