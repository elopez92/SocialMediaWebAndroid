package manic.com.twitterwebservice.service

import android.telecom.Call

interface Webservice {
    /**
     * @GET declares an HTTP GET request
     * @Path("user") annotation on the userId parameter marks it as a
     * replacement for the {user} placeholder in the @GET path
     */
    //@GET("/users/{user}")
    //fun getUser(@Path("user") userId: String): Call<User>
}