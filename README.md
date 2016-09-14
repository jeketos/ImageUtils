# ImageUtils

## Usage:
1. Add library to your project
2. In `AndroidManifest.xml` write in your `<application>` next:

 ```xml
     <activity android:name="com.idap.imageutils.ImageUtilsActivity"/>
 ```
 
3. If you want take photo from camera:

 ```java
     ImageUtils.takePhoto({activity_or_fragment}, {YOUR_REQUEST_CODE});
 ```
 from device:
 ```java
     ImageUtils.getImageFromGallery({activity_or_fragment}, {YOUR_REQUEST_CODE});
 ```
 If your `targetSdkVersion 23` or higher dont forget about `Runtime Permissions`.

4. In onActivityResult do next

 ```java
   if(requestCode == {YOUR_REQUEST_CODE} && resultCode == Activity.RESULT_OK){
        imagePath = data.getExtras().getString(Constants.BUNDLE_KEY);
   }
 ```
 Path to image: `/storage/emulated/0/Android/data/{your_project}/files/tmp/`
  
##Publish:
###1) ./gradew install
###2) ./gradew bintrayUpload
