package dev.sotoestevez.allforone.util.webserver

const val AUTH_SIGNIN_200: String = "{\"session\":{\"auth\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzZXNzaW9uSWQiOiI2MTE5OGZmMjQwY2VjMzA2N2E2NmMwYjEiLCJpYXQiOjE2MjkwNjUyMDIsImV4cCI6MTYyOTA2ODgwMn0.g-5EN1u69zj0c3mVVI4zKiQfuy-OIKqa3pIqWiHXlqk\",\"refresh\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzZXNzaW9uSWQiOiI2MTE5OGZmMjQwY2VjMzA2N2E2NmMwYjEiLCJpYXQiOjE2MjkwNjUyMDIsImV4cCI6MTYyOTE1MTYwMn0.u052QG8_ktGJC077CHbH8cRrNRDf4m1K-RM4O9QbXrU\",\"expiration\":1629068802},\"user\":{\"_id\":\"61198ff240cec3067a66c0b1\",\"googleId\":\"valid\",\"role\":\"blank\",\"__v\":0}}"
const val AUTH_SIGNIN_400: String = "{\"message\":\"The specified user does not have a valid ID\"}"
const val FEED_MESSAGES_EMPTY_200: String = "{\"messages\":[{\"message\":\"message\",\"user\":\"id\",\"username\":\"name\",\"timestamp\":0,\"type\":\"text\"},{\"message\":\"message1\",\"user\":\"id1\",\"username\":\"name1\",\"timestamp\":1,\"type\":\"task\",\"room\":\"room\"}]}"
const val USER_BOND_ESTABLISH_200: String = "{\"message\":\"The bond has been established\"}"
const val USER_BOND_ESTABLISH_401: String = "{\"message\":\"The provided token is invalid\"}"
const val USER_BOND_GENERATE_200: String = "{\"code\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\"}"
const val USER_BOND_LIST_200: String = "{\"bonds\":[{\"role\":\"blank\",\"displayName\":\"First\",\"mainPhoneNumber\":\"123456789\"},{\"role\":\"keeper\",\"displayName\":\"Second\",\"mainPhoneNumber\":\"987654321\"}]}"
const val USER_CARED_DEFINED_200: String = "{\"cared\":{\"_id\":\"61198ff240cec3067a66c0b1\",\"googleId\":\"valid\",\"role\":\"patient\",\"__v\":0}}"
const val USER_CARED_UNDEFINED_200: String = "{\"cared\":null}"
const val USER_UPDATE_201: String = "{\"message\":\"The specified user has been updated successfully\"}"
const val USER_UPDATE_401: String = "{\"message\":\"The provided token is invalid\"}"