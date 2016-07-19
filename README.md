MultiBackStack
==============

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-MultiBackStack-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3636)
[![Release](https://jitpack.io/v/jetradarmobile/multibackstack.svg)](https://jitpack.io/#jetradarmobile/multibackstack)
[![Build Status](https://travis-ci.org/JetradarMobile/multibackstack.svg?branch=master)](https://travis-ci.org/JetradarMobile/multibackstack)

[![AndroidDev Digest](https://img.shields.io/badge/AndroidDev%20Digest-%2396-blue.svg)](https://www.androiddevdigest.com/digest-96/)

Implementation of the multiple back stacks. Using this library allows implementation of persistent [bottom navigation][1] like in [instagram][2].

![image](https://raw.githubusercontent.com/JetradarMobile/multibackstack/master/art/img_promo.jpg)


Compatibility
-------------

This library is compatible from API 9 (Android 2.3).


Download
--------

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

Add the dependency

```groovy
dependencies {
    compile 'com.github.jetradarmobile:multibackstack:1.2.0'
}
```


License
-------

    Copyright 2016 JetRadar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[1]: https://www.google.com/design/spec/components/bottom-navigation.html
[2]: https://play.google.com/store/apps/details?id=com.instagram.android
