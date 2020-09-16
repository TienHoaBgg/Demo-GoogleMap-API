package com.nth.demomapapi.model

/**
 * Created by Nguyen Tien Hoa on 09/16/2020
 */
 
 

data class RoutesObj(var bounds:Any, var copyrights:String, var legs:MutableList<LegsObj>, var overview_polyline:PolylineObj, var summary:String)