# Parse-Server-starter

## With back4app

## [Starting Guide](https://www.back4app.com/docs/get-started/welcome)
- 1.Create a new app in Back4App
-	2.Get App Keys under ***```App Settings-> Security & Keys```***
-	3.Parse Android SDK Installation
	-	go to the ```build.gradle (Module:app)```
    ```
    dependencies {
      // code...
      // Don't forget to change the line below with the latest version of Parse SDK for Android
      implementation "com.github.parse-community.Parse-SDK-Android:parse:4.2.0"
    }

    repositories {
      mavenCentral()
      jcenter()
      maven { url 'https://jitpack.io' }
    }
    ```
	
	-	[Latest version](https://jitpack.io/#parse-community/Parse-SDK-Android)
-	4.Initialize the SDK using your *** Application ID *** and *** Client Key *** *** inside the ```strings.xml``` ***
  
    ```./app/src/main/res/values/strings.xml```
    ```
    <resources>
      <string name="back4app_server_url">https://parseapi.back4app.com/</string>
      <!-- Change the following strings as required -->
      <string name="back4app_app_id">APP_ID</string>
      <string name="back4app_client_key">CLIENT_KEY</string>
    </resources>
    ```
		
- 5.Initialize Parse SDK
  - Create a Java file called App that extends Application. Inside ***```ParseApp.java```*** onCreate method, right after ***```super.onCreate()```*** initialize ***Parse***.

	```ParseApp.java ```
	```
	import android.app.Application;
	import com.parse.Parse;

	public class ParseApp extends Application{
	    @Override
	    public void onCreate() {
		super.onCreate();

		//Initializing connection at onCreate
		Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
			.applicationId(getString(R.string.back4app_app_id))
			.clientKey(getString(R.string.back4app_client_key))
			.server(getString(R.string.back4app_server_url))
			.build());
	    }
	}
	```
	
- 6.Give Permissions and set up your App
	 -  ```./app/src/main/AndroidManifest.xml```
    ```
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    ```
	  - inside the application tag, add the following:

    ```
    <application
        android:name=".ParseApp"
        .
        .
        <meta-data
          android:name="com.parse.SERVER_URL"
          android:value="@string/back4app_server_url" />
        <meta-data
          android:name="com.parse.APPLICATION_ID"
          android:value="@string/back4app_app_id" />
        <meta-data
          android:name="com.parse.CLIENT_KEY"
          android:value="@string/back4app_client_key" />
          .
          .
    </application>
    ```
	
## Tutorial

## Upload Data

```
//Score is the table name. if 'Score' does not exist then its create one.
ParseObject score = new ParseObject("Score");
score.put("userName","Sakib"); //Column name and value
score.put("score",45);
score.saveInBackground(new SaveCallback() { //upload file to the parse server
    //using this call back function will return extra information like if it failed or succeed to upload the file
    @Override
    public void done(ParseException e) {
        if (e==null) { // no error occurred
            Log.i("MainActivity","Succeed");
        } else {
            e.getMessage();
            e.getStackTrace();
        }
    }
});
```

## Fetch Single Data

```
ParseQuery<ParseObject> query = ParseQuery.getQuery("table name");
query.getInBackground("objectId", new GetCallback<ParseObject>() { //objectId is the primary key
    @Override
    public void done(ParseObject object, ParseException e) {
        if (e==null && object != null) {
            object.get("userName"); //or
            object.getString("userName");
        }
    }
});
```

## Read Object

```
ParseQuery<ParseObject> query = ParseQuery.getQuery("Table name");
query.orderByDescending("createdAt");
query.findInBackground((objects, e) -> {
    progressDialog.dismiss();
    if (e == null) {
        //passing object list to other function like adapter
        //initTodoList(objects);
        List<ParseObject> list = objects;
    } else {
        showAlert("Error", e.getMessage());
    }
});
```

## Update Data

```
//get the whole row of the data
ParseQuery<ParseObject> queryU  = new ParseQuery<ParseObject>("Table name");
queryU.getInBackground("objectId", new GetCallback<ParseObject>() {
    @Override
    public void done(ParseObject object, ParseException e) {
        if (e == null) {
            object.put("Column name","Value");
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        //update success
                    } else {
                        e.getStackTrace();
                    }
                }
            });
        } else {

        }
    }
});
```

## Delete Object

```
//Delete a Row--------------------------
ParseQuery<ParseObject> queryD = ParseQuery.getQuery("Table Name");
queryD.getInBackground("objectId", new GetCallback<ParseObject>() { //objectId is the primary key
    @Override
    public void done(ParseObject object, ParseException e) {
        if (e==null && object != null) {
            object.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                       //delete success
                    } else {
                       e.getMessage();
                    }
                }
            });
        }
    }
});
```

## Advance query

```
//SELECT * FROM 'TABLE' WHERE 'COLUMN' = 'VALUE' LIMIT 1 ; --implementation
ParseQuery<ParseObject> query1  = new ParseQuery<ParseObject>("Table name");
query1.whereEqualTo("column_name"," value");
query1.whereEqualTo("note","some note");
query1.whereGreaterThan("","");
query1.setLimit(1);
query1.findInBackground(new FindCallback<ParseObject>() {
    @Override
    public void done(List<ParseObject> objects, ParseException e) {
        if (e==null){
            if (objects.size()>0){
                String objectId = "";
                for (ParseObject object:objects){
                    objectId = object.getObjectId();
                }
            }
        }

    }
});
```

## File or Image upload

```
ParseObject var = new ParseObject("tableNmae");

//location upload
ParseGeoPoint pGeoPoint = new ParseGeoPoint( m.getPosition().getLatitude(), m.getPosition().getLongitude());
var.put("location", pGeoPoint);

//file upload----------
//before uploading the file or the image needs to converted into "byte array"
byte[] data = null;
//chosing image from image galary
Bitmap bitmap = ((BitmapDrawable) chosenImageDialogBOx.getDrawable()).getBitmap();
ByteArrayOutputStream baos = new ByteArrayOutputStream();
bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//image to byte array corvertion for uploading
data = baos.toByteArray();
ParseFile parseFile = new ParseFile("image-", data);
var.put("image", parseFile)

fountainAddToServer.saveInBackground(new SaveCallback() { //upload file to the parse server
    //using this call back function will return extra information like if it failed or succeed to upload the file
    @Override
    public void done(ParseException e) {
        if (e==null) { // no error occurred
           //upload successful
        } else {
            e.getMessage();
            e.getStackTrace();
        }
    }
});
```
                

## Signup parse user

```
ParseUser user = new ParseUser();
user.setUsername(name.getText().toString());
user.setPassword(password.getText().toString());
user.signUpInBackground(new SignUpCallback() {
    @Override
    public void done(ParseException e) {
        if (e==null){
            Toast.makeText(getApplicationContext(),"Sign up success", Toast.LENGTH_SHORT).show();
            startActivity();
        } else {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
});
```

## Login

```
ParseUser.logInInBackground("userName", "passWord", new LogInCallback() {
    @Override
    public void done(ParseUser user, ParseException e) {
	if (user != null){
	    Toast.makeText(getApplicationContext(),"login successful", Toast.LENGTH_SHORT).show();
	    startActivity();
	} else {
	    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
	}
    }
});
```

## Check login
```
ParseUser currentUser = ParseUser.getCurrentUser();
if (currentUser != null) {
    // do stuff with the user

} else {
    //user not logged in
    // show the signup or login screen

}
```
