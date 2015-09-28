Brandon Ginoza, Almog Boanos, Herbie Torrance

# gitHound System

This is a system that allows you to find all of the git repositories in 
a directory that you specify. gitHound will can tell you a quick summary of all the 
repositories in that directory or can give you a summary of each individual 
repository in the directory. Another feature gitHound has is it allows you to
expand the status of any one of the directories you specify.

# How to get

Create a new directory in your terminal and then initialize it with the command: 

```shell
git init
```

After running this command, then clone our repository by running this command: 

```shell
git clone https://github.com/ginozab/gitHound.git
```

This will then give you full access to our code. 

# How to use

Running our system is pretty simple. It needs two arguments: 

Argument 1: "Path/to/the/directory" (Relative to the current working directory)

Argument 2: "quick" or "full"

Specifying "quick" will give you the quick summary statistics of all the git repositories
inside of the specified directory. By specifying "full", it will give you a more detailed 
summary of every git repository inside of the specified directory, this also gives you the 
option to expand the status of a given git repository.

# Example input

```shell
javac gitHound.java
java gitHound ../../path/to/directory quick
```

or 

```shell
javac gitHound.java
java gitHound ../ full
```

# Expand Feature

The expand feature allows you to see exact details of a specified git repository. This 
feature is only accessible while running a full summary not a quick summary. After running the 
full summary it will ask you if you want to see an expanded status, and then prompt you to enter
"yes" or "no".

If yes, it will then prompt you to enter a working directory. Please choose from one of the 
git repositories it showed in the full summary, do so by just copying and pasting a given directory. 

If no, it will say "No expansion and terminate the program".

# Video Example

[![asciicast](https://asciinema.org/a/5mkifdwolsi6eaaow2kefmgzr.png)](https://asciinema.org/a/5mkifdwolsi6eaaow2kefmgzr)

# Thank you for choosing gitHound!




