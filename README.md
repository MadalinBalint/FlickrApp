# FlickrApp

Single activity Android app that shows photos using Flickr's REST APIs from search results by tags.

## Features
- **Search view** to get results by tags, shown inside a vertical RecyclerView with infinite scrolling

## Android & 3rd party features used
- Kotlin programming language
- material components & themes
- light & dark themes
- single activity with fragment
- MVVM with LiveData and ViewModel
- navigation components
- vertical RecyclerView with PagingAdapter & DiffUtil
- endless scrolling with PagingDataAdapter and RxPagingSource, using Paging v3
- swipe to refresh  
- data binding, view binding, binding adapters for loading images
- REST API calls with [Retrofit](https://square.github.io/retrofit/)
- image loading with [Glide](https://github.com/bumptech/glide)
- dependency injection with [Hilt](https://dagger.dev/hilt/)
- retry mechanism in case of *5xx* server errors
- RxJava calls
