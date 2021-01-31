# Top-Ten Mobile App Group Project

## Introduction
Top Ten is an engaging Android polling app in which users can vote on a list of items, ultimately forming a “definitive” top ten ranking for the items. Users can rank their favorite fast-food restaurants, movies, albums, and much more. 

* Each day, a new list items is released, sparking debate among users as they advocate for their favorite items. 
* Users will receive a notification each morning as the list of the day is about to be released.
* Before voting begins, users can predict which item will come out on top, earning points if guessing correctly.

## Storyboard
[Storyboard - On InVision] (https://projects.invisionapp.com/prototype/TopTenDesign-ckkjcz4cd001f9s01t91hwjht/play/afdaa50e)

## Functional Requirements

### Requirement 01: Vote on an item

#### Scenario
As a user interested in voting on the list of the day, I want to vote on my favorite item so that I can see my response compared to to others, and earn points to reddem in the app.

#### Dependencies
The list items can be retrieved and parsed from Firebase. User's votes can be stored in Firebase.

#### Assumptions
Lists of the day are available and created by the development team.

#### Examples

1.1\
Given that Cinnamon Toast Crunch is tied for first place on a list of favorite cereals\
When a user votes for Cinnamon Toast Crunch\
Then the Cinnamon Toast Crunch list item should move to the top of the list on the UI\

1.2\
Given that a user with one vote attempts to vote for Cherrios on a list of favorite cereals\
When they have already voted on Cinnamon Toast Crunch/
Then the vote they placed on Cheerios should be removed, and added to the tally for Cinnamon Toast Crunch\


## Class Diagram

![Class Diagram](https://user-images.githubusercontent.com/41589695/106334986-d5c31f00-62b1-11eb-8f16-62727074d743.png)  


## Class Diagram Description

**MainActivity:** The first screen the user sees. This will have a list of all the list items of the day, and an option to vote for the user's favorite list item.  

**RetrofitInstance:** Boostrap class required for Retrofit.  

**ListItem:** Noun class that represents a list item.  

**IListItemDAO:** Interface for Retrofit to find and parse List Item JSON.  

## Product Backlog
*Located under this repository's GitHub Projects tab*

## Scrum Board
*Located under this repository's GitHub Projects tab*


## Scrum Roles
**Project Owner**: Isaiah Dicristoforo\
**Integration Specialists**: Christian Turner and Atharva Chalke\
**UI Designer**:  Colin Vitols and Matthew Willison


## Group Standup
*Our weekly meetings will be held on Tuesdays at 9:30 AM on a Discord server we have set up for the group project. Other meetings will be scheduled as necessary*
