Reyes Social Media/Twitter Application Project

This is a JavaFX GUI application that allows people to interact with others and share media and 
messages as posts across the application. As a user you are able to personalize your experience on the app and follow any users on the app.

This GUI app uses a selection of data structures to make the app scalable and efficient regardless of the data size.
This is possible with the use of:


AppData class is used as a singleton class to o control object creation, limiting the number to one instance while the app is running.

Within this AppData:
A single TreeMap <Username, User> userData: uses a String username and a User class as a key-value pair 
to hold all the users data. The TreeMap helps the app keep usernames unique and makes it very quick to find a specific user data.
If a user tries to make a an account with an existing username, user will get an error and told username is taken. The TreeApp also allows app to have an indefinite number of users.

A LinkedList<Posts> globalPosts: uses the posts class in a linked list to create the posts across 
the whole application. Very important to keeping the chronological order of all posts and allowing the app to have an indefinite number of posts.

longs userCount, postCount are used to record the total posts and users even if the user is deleted or a post is removed.

DataCenter class:
The AppData class is called to a data center where all the data is handled at run.
The AppData class written to a file and saved when the user logs out and load the data when the user logs in.

This DataCenter has the apps following functions:
/*Backend at run*/ 
loadData, saveData, logout, loadAccountInfo, loadUserPosts, updatingData(feed, explore, home)
updateDictionary(only when new words are added).

/*SignUp/CreatingNewUser functions*/
Confirm username and password
userExists
addUser

/*Login functions*/
loginUser

/*App functions*/
addMessagePost
addImagePost
addReply
followUser
unfollowUser
likePost
unlikePost
isLiked

/* User Functions */
discoverUsers
spellCheckPost
userSearch
getUserDetails
getAccountDetails
getGlobal
getMyFeed
getMyPosts


/*Util*/ class 
getPostTime
getPostDate
getLikeCount
importDictionary
updateDictionary
getPfps
validatorsForTextInputs