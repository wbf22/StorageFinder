# Storage Finder

I've had some problems in the past, when my computer runs out of storage and I can't figure out where all the space has been taken up.

This little command line app searches your files and figures out where the majority of your storage is taken up. 

## Usage
I included a built jar in th repo. If you have java 17 (or a greater version probably) you can run the jar with:

```shell
java -jar StorageFinder-1.0-0.jar
```

You can also just open up this project your project in your favorite IDE and run the main file [StorageFinder.java](src%2Fmain%2Fjava%2Fbf%2Fstorage%2Ffinder%2FStorageFinder.java)


## Instructions

After starting up the app, you'll see this prompt if the app can find your home directory:
```
Scan from home directory: '/Users/<currentUser>'?(y/n)
```
If this got your home directory, you can just enter 'y' to continue. Otherwise you can enter 'n' and type in the path to whatever folder you want to start scanning from. 

You'll then see something like this:
```
Scanning Directories: [=========================...........................................................................] 25%
```
Here the app is going through all your files and folders that the os allows it to access, and totaling up the size of each one. You'll then see this:
```
Enter Gb size:
```
Enter in a number for the size in GB you'd like to search for. The app will then show you all files and folders that are larger than that size. 
```
Directories/Files over 10.0 Gb: 
64.0 Gb/Users/brandon.fowler/Library/Containers/com.docker.docker/Data/vms/0/data/Docker.raw
12.7 Gb/Users/brandon.fowler/Documents
```

## Notes
I haven't tested this on windows or linux yet. It works so far on mac. 


## Developers
Feel free to rip anything or reuse this code however you like. I organized the packages to seperate the 'core' logic and file scanning from the terminal ui code, so the 'bf/storage/finder/core' folder could be ripped out and reused for an app with an actual ui instead of a terminal. 

[StorageFinder.java](src%2Fmain%2Fjava%2Fbf%2Fstorage%2Ffinder%2FStorageFinder.java) controls the ui and uses [FileExplorerService.java](src%2Fmain%2Fjava%2Fbf%2Fstorage%2Ffinder%2Fcore%2Fservice%2FFileExplorerService.java) to scan files and [StorageFinderService.java](src%2Fmain%2Fjava%2Fbf%2Fstorage%2Ffinder%2Fcore%2Fservice%2FStorageFinderService.java) to figure out which ones to display.

You'd probably end up needing a different flow though so here's some tips to find the meat of the app: 

This gets a list of all files and subdirectories with their totaled size depth relative to the scan start:
```java
FileExplorerService explorer = new FileExplorerService(file, 10000, ui );
    
List<FileData> files = explorer.exploreFile(file, 0);
```


The 'getResults' method in [StorageFinderService.java](src%2Fmain%2Fjava%2Fbf%2Fstorage%2Ffinder%2Fcore%2Fservice%2FStorageFinderService.java)
iterates over the files and figures out what directories/files for the specified search Gb size.

