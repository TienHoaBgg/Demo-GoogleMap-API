package com.nth.demomapapi.model

/**
 * Created by Nguyen Tien Hoa on 09/16/2020
 */
 
 

data class DirectionsObj(var geocoded_waypoints:Any, var routes:MutableList<RoutesObj>, var status:String)