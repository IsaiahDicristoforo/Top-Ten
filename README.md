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

### Requirement 02: Earn Points for making correct predictions about the list of the day

#### Scenario
As a user interesting in earning points in this app, I want to make a prediction about a list before voting goes live so that I will earn points if my prediction is correct.

#### Dependencies
Firebase, timed notifications and events so that the user can make predictions before a list of the day goes live.

#### Assumptions
User's predictions can be stored in firbase, and compared with the final results after voting on a list ends.

#### Examples

1.1\
Given that a list of cereals is available for people to make predictions on before voting opens up\
When a user predicts that Cinnamon Toast Crunch will be the most voted on Cereal\
Then if after voting ends Cinnamon Toast Crunch is the most voted on cereal, the user should receive 1000 points.\

1.2\
Given that a list of cereals is available for people to make predictions on before voting opens up\
When a user predicts that Cinnamon Toast Crunch will be the most voted on Cereal\
Then if after voting ends Cinnamon Toast Crunch is NOT the most voted on cereal, the user should lose 500 points.\

### Requirement 03: Redeem points for prizes and extra votes

#### Scenario
As a user, after making correct predictions about how other users will vote, I want to redeem the points I earn for prizes and bonuses in the app such as an extra vote.

#### Dependencies
Firebase login system to assign points to a user account.

#### Assumptions
User's have an account to which points can be awarded. User's can make predictions on the list of the day before voting opens to earn points.

#### Examples

1.1\
Given that a user has earned 5,000 points by making correct predictions\
When a user buys the "extra vote" bonus from the rewards page\
Then the user should be able to vote twice when the next list goes live \

1.2\
Given that a user has no points earned in the app\
When a user attempts to buy the "extra vote" bonus from the rewards page\
Then their attempt to make the purchase should be rejected, and they should only be able to vote once on the next list.\


## Class Diagram

![ClassDiagram](https://user-images.githubusercontent.com/41589695/106377762-6e968f00-63c5-11eb-924d-bdcc6d7f7787.png)  


## Class Diagram Description

**MainActivity:** The first screen the user sees. This will have a list of all the list items of the day, and an option to vote for the user's favorite list item.  

**RetrofitInstance:** Boostrap class required for Retrofit.  

**ListItem:** Noun class that represents a list item.  

**IListItemDAO:** Interface for Retrofit to find and parse List Item JSON.  

**PreviousTopRated:** Noun class that represents a previously top rated item.  

**IPreviousTopRated:** Interface for Retrofit to find and parse Previously Top Rated List Item JSON.  

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
