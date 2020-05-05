from locust import HttpLocust, TaskSet, between
import json

body = {
  "name": "string"
}

headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0YWJjIiwiZXhwIjoxNTg4NzUwNTIwLCJpYXQiOjE1ODg3MTQ1MjB9.2HIEHuszIB94wEoPD18xI8DPTUbgefm-Phj46ppDpmM'}

def get(l):
    l.client.get("/ahmet1")

def login(l):
	l.client.post("/auth/Authenticate", {"username" : "testabc" ,
											"password" : "testabc"})

def save(l):
    l.client.post("/create/URL?userName=testabc&longURL=www.facebook.com", headers=headers)

class UserBehavior(TaskSet):
    tasks = {get: 100, save: 1}

class WebsiteUser(HttpLocust):
    task_set = UserBehavior
    wait_time = between(5, 9)
