from locust import HttpLocust, TaskSet, between
import json

body = {
  "name": "string"
}

headers = {'Content-Type': 'application/json', 'Accept': 'application/json'}

def get(l):
    l.client.get("/getAll?userName=yusuf")

def save(l):
    l.client.post("/create?userName=ali&longURL=ali.com&number=5", data=json.dumps(body), headers=headers)

class UserBehavior(TaskSet):
    tasks = {get: 100, save: 1}

class WebsiteUser(HttpLocust):
    task_set = UserBehavior
    wait_time = between(5, 9)
