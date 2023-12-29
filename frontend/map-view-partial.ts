//Bing Sign up and Generate an API Key: https://www.bingmapsportal.com/

// Create file API-key.ts here in frontend folder with the uncommented lines below:
// const APIkey: string = "The-Bing-Maps-APIkey";
// export default APIkey;
import APIkey from "./API-key";
import SearchManager = Microsoft.Maps.Search.SearchManager;
declare global { interface Window {GetMap : any, ShowMap : any }}


// import {waitUntil} from "workbox-core/_private";

// var map , searchManager;
// var mapsApiUrl = "https://www.bing.com/api/maps/mapcontrol?callback=GetMap";
// var locationString;
var map : Microsoft.Maps.Map, searchManager : SearchManager;
var mapsApiUrl : string = "https://www.bing.com/api/maps/mapcontrol?callback=GetMap";
var locationString : string;

window.GetMap = async function getMap() {


    // @ts-ignore
    map = await new Microsoft.Maps.Map(document.getElementById('myMap'),
        {
            credentials: APIkey
        });



    // console.log(map);

    //Make a request to geocode New York, NY.
    console.log(locationString + '  --- PROPERTY NULL ??? ----');

    await geocodeQuery(locationString);
}

function geocodeQuery(query : string) {
    //If search manager is not defined, load the search module.
    if (!searchManager) {
        //Create an instance of the search manager and call the geocodeQuery function again.

        Microsoft.Maps.loadModule('Microsoft.Maps.Search', async function () {
            searchManager = new Microsoft.Maps.Search.SearchManager(map);
            geocodeQuery(query);
        });
    } else {
        var searchRequest = {
            where: query,
            callback: function (r? : any) {
                //Add the first result to the map and zoom into it.
                if (r && r.results && r.results.length > 0) {
                    var pin = new Microsoft.Maps.Pushpin(r.results[0].location);
                    map.entities.push(pin);

                    map.setView({ bounds: r.results[0].bestView });
                }
            },
            errorCallback: function (e : any) {
                //If there is an error, alert the user about it.
                alert("No results found.");
            }
        };

        //Make the geocode request.
        searchManager.geocode(searchRequest);
        // window.addEventListener("load", function () {  });
    }
}

window.ShowMap = function showMap(location: string){
    var scripts = document.querySelectorAll(".map-per-request");
    if (scripts.length === 0){
    // scripts.forEach(scrp => {if(scrp.parentElement!=null) scrp.parentElement.removeChild(scrp)});

        var head = document.getElementsByTagName("head")[0];

        locationString = location;
        var script = document.createElement("script");
        script.className = "map-per-request";
        script.type = "text/javascript";
        script.async = true;
        script.defer = true;
        script.src = mapsApiUrl;

        script.onload = window.GetMap;

        head.appendChild(script);
    }
    else geocodeQuery(location)
    var cross_origin_scripts = document.querySelectorAll("script[crossorigin=\"anonymus\"]");
    cross_origin_scripts.forEach(sc => {if(sc.parentElement!=null) sc.parentElement.removeChild(sc)});
}








