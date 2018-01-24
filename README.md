# WardrobeApp
This is the code of the Wardrobe App. It is written in Kotlin based on MVVM architecture.

Wardrobe is an app that suggests you combinations of clothes for daily wear. The home screen is divided into two sections, each displaying a shirt (top) and a pair of trousers (bottoms). You can add more shirts or pants by clicking on the ‘+’ button on the right corner of each section. You can also swipe left and right each section if you don’t like the shirt or pants. In the centre, there's a 'shuffle’ button. On its right is a ‘favourite’ button if you like an outfit combination. The screens can be vertical or horizontal, depending on the portrait or landscape support.


Additional Assumptions :
- Shuffle or Bookmark or Notification won’t work until we have until one shirt and one pant.
- Shuffle has two random logic implemented as nothing was specified as to the type of the logic to be implemented.

Requiremenets :
1. Home Screen (the above wireframes) with a neat UI.

2. Portrait and landscape support with preserving the state of the app.

3. Support for both camera and gallery to add shirts and pants (need DB support).

4. You can swipe through both shirts and pants horizontally to see new combination.

4. Shuffle button to display random combination of shirt and pants. Need a basic algorithm to show a new combination every time. Basically when to choose from the favourite section or giving completely new combination and all.

5. 'Favourite' button to mark combination as favourite (need DB support)

6. Notification: app should send a notification for the new combination of shirt and pants everyday in the morning at 6 am.

WireFrames :

![First WireFrame](https://raw.githubusercontent.com/legalimpurity/WardrobeApp/master/IMG_3899.JPG)
![Second WireFrame](https://raw.githubusercontent.com/legalimpurity/WardrobeApp/master/IMG_3900.JPG)
