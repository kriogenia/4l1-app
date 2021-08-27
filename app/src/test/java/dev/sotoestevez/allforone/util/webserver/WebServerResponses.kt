package dev.sotoestevez.allforone.util.webserver

const val AUTH_SIGNIN_200: String = "{\"session\":{\"auth\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzZXNzaW9uSWQiOiI2MTE5OGZmMjQwY2VjMzA2N2E2NmMwYjEiLCJpYXQiOjE2MjkwNjUyMDIsImV4cCI6MTYyOTA2ODgwMn0.g-5EN1u69zj0c3mVVI4zKiQfuy-OIKqa3pIqWiHXlqk\",\"refresh\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzZXNzaW9uSWQiOiI2MTE5OGZmMjQwY2VjMzA2N2E2NmMwYjEiLCJpYXQiOjE2MjkwNjUyMDIsImV4cCI6MTYyOTE1MTYwMn0.u052QG8_ktGJC077CHbH8cRrNRDf4m1K-RM4O9QbXrU\",\"expiration\":1629068802},\"user\":{\"_id\":\"61198ff240cec3067a66c0b1\",\"googleId\":\"valid\",\"role\":\"blank\",\"__v\":0}}"
const val AUTH_SIGNIN_400: String = "{\"message\":\"The specified user does not have a valid ID\"}"
const val USER_UPDATE_201: String = "{\"message\":\"The specified user has been updated successfully\"}"
const val USER_UPDATE_401: String = "{\"message\":\"The provided token is invalid, please log in again\"}"