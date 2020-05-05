from locust import HttpLocust, TaskSet, between
import json

body = {
  "name": "string"
}

headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MTJiYSIsImV4cCI6MTU4ODcyNzk3MiwiaWF0IjoxNTg4NjkxOTcyfQ.tajNRqODf_6TK_nW3Ai-gBqMYJxPMve8dLDDnBel2Ig'}

def get(l):
    l.client.get("/ahmet")

def login(l):
	l.client.post("/auth/Authenticate", {"username" : "test12ba" ,
											"password" : "test12ba"})

def save(l):
    l.client.post("/create/URL?userName=test12ba&longURL=www.facebook.com", headers=headers)

class UserBehavior(TaskSet):
    tasks = {get: 100, save: 1}

class WebsiteUser(HttpLocust):
    task_set = UserBehavior
    wait_time = between(5, 9)
