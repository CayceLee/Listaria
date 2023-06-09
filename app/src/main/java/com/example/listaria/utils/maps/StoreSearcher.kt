package com.example.listaria.utils.maps

//class StoreSearcher(private val context: Context) {
//
//    private lateinit var placesClient: PlacesClient
//    private var fusedLocationProviderClient: FusedLocationProviderClient
//    val radius = 15
//    private var executor: ScheduledExecutorService? = null
//    private var searchRunnable: Runnable? = null
//
//
//    init {
//        // Initialize Places API client
//        Places.initialize(context, MAPS_API_KEY)
//        // Initialize Location Provider client
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
//    }
//
//    fun startLocationSearch(location: String, list: ListOfListsData) {
//        // Create a scheduled executor to run the location search
//        executor = Executors.newSingleThreadScheduledExecutor()
//        executor?.scheduleAtFixedRate({
//            val geocoder = Geocoder(context, Locale.getDefault())
////            val results = geocoder.getFromLocationName(location, 1)
////                if (results.isNotEmpty()) {
//                    val notificationSender = NotificationSender(context)
//                    System.out.println("Notification function is called")
//                    notificationSender.sendNotification("Listaria", "You may have a list you forgot about", list)
////                }
//        }, 0, SEARCH_INTERVAL, TimeUnit.MILLISECONDS)
//    }
//
//    fun stopLocationSearch() {
//        // Stop the scheduled executor
//        executor?.shutdown()
//    }
//
//
//    @SuppressLint("MissingPermission")
//    fun nearbyStoreSearch(store: MutableList<String>, listsData: MutableList<ListOfListsData>) {
//        // Check for Location permission
//
//            // Get the current location
////            val locationTask: Task<Location> = fusedLocationProviderClient.lastLocation
////            placesClient = Places.createClient(context)
////            val placeFields = listOf(
////                Place.Field.ID,
////                Place.Field.NAME,
////                Place.Field.ADDRESS,
////                Place.Field.LAT_LNG
////            )
//
////            locationTask.addOnSuccessListener {
////                val request = FindCurrentPlaceRequest.builder(placeFields).build()
//
////                placesClient.findCurrentPlace(request).addOnSuccessListener { response ->
////                    for (placeLikelihood in response.placeLikelihoods) {
////                        val place = placeLikelihood.place
//
////                        val startPoint = Location("locationA")
////                        startPoint.latitude = place.latLng?.latitude!!
////                        startPoint.longitude = place.latLng?.longitude!!
////
////                        val endPoint = Location("locationB")
////                        endPoint.latitude = place.latLng?.latitude!!
////                        endPoint.longitude = place.latLng?.longitude!!
//
//                        val startPoint = Location("locationA")
//                        startPoint.latitude = 33.538479
//                        startPoint.longitude =  -112.098751
//
//                        val endPoint = Location("locationB")
//                        endPoint.latitude = 33.538479
//                        endPoint.longitude =  -112.098751
//
//                        val distance = startPoint.distanceTo(endPoint).toDouble()
//                        System.out.println("Distance: $distance, Radius: $radius")
//                        if (store.isNotEmpty() && distance <= radius) {
//                            // The place is a park within the search radius
//                            for (i in 0 until store.size) {
//                                startLocationSearch(store[i], listsData[i])
//                                System.out.println("Made it to startLocationSearch")
//                            }
//                        }
////                    }
////                }.addOnFailureListener {
////                    stopLocationSearch()
////                }
////            }
//    }
//
//    companion object{
//        private const val SEARCH_INTERVAL = 1800000L // 30 minutes
////        private const val SEARCH_INTERVAL = 10000L // 10 seconds
//    }
//
//}


