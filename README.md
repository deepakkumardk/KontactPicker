# KontactPicker
[![](https://jitpack.io/v/deepakkumardk/KontactPicker.svg)](https://jitpack.io/#deepakkumardk/KontactPicker)
[![](https://img.shields.io/badge/Android%20Arsenal-KontactPicker-brightgreen)](https://android-arsenal.com/details/1/7836)

An Contact Picker library for Android, written purely in [Kotlin](http://kotlinlang.org) with Co-routines. KontactPicker is:

 - **Fast**: KontactPicker is very fast, gives you the contact list by querying the Content Providers in a optimized way.
 - **Customizable**: You can customize the UI of this library as you want including theme, SelectionView, ImageMode etc.
 - **Modern**: It's a Kotlin first and uses the Co-routines library with AndroidX artifacts.


<img src="screenshots/KontactPickerDemo.gif" alt="Demo Sender" align="center" width="40%" hspace="20" title="Sender" />


## Usages
project/build.gradle
```groovy
allprojects {
	repositories {
	    maven { url 'https://jitpack.io' }
	}
}
```

app/build.gradle
```groovy
dependencies {
        implementation 'com.github.deepakkumardk:KontactPicker:$latest-version'
}
```

### Activity

**NOTE: This library is based on AndroidX artifacts**

**No need** to specify the **READ_CONTACT** permission in your manifest file, library will handle this permission internally.

```kotlin
    KontactPicker().startPickerForResult(this, KontactPickerItem(), 3000)  //RequestCode
```

If you want to start this library from a fragment just pass the fragment context in the above method.


### Customization
```kotlin
    val item = KontactPickerItem().apply {
        debugMode = true
        includePhotoUri = true          //Default is false, If you want to include Uri in the result list
        imageMode = ImageMode.TextMode                      //Default is None
        themeResId = R.style.CustomTheme                    //Default is Dark Theme
        selectionTickView = SelectionTickView.LargeView     //Default is SmallView
        textBgColor = ContextCompat.getColor(this@MainActivity, R.color.colorPrimary)  //Default is Random Color
    }
    KontactPicker().startPickerForResult(this, item, 3000)  //RequestCode
```

#### Handing Results
```kotlin
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 3000) {
            val list = KontactPicker.getSelectedKontacts(data)  //ArrayList<MyContacts>
            //Handle this list
        }
    }
```
### Fetch Contacts
If you want to fetch only contacts from Phonebook and don't want to use the default UI from this
library you can also do it with this method.

```kotlin
    KontactPicker.getAllKontacts(this) { contactList - >
        //Handle the contactList : MutableList<MyContacts>
    }
```

Above method doesn't give the contact photo Uri, So, if you want that too use the below method,
which is also as fast as the above.

```kotlin
    KontactPicker.getAllKontactsWithUri(this) { contactList - >
        //Handle the contactList : MutableList<MyContacts>
    }
```

## License

```
 Copyright 2019 Deepak Kumar

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   ```
