package edu.uc.groupProject.topten.DTO

data class ListItem(var title:String, var description:String="", var totalVotes:Int){

    override fun toString(): String {
        return title;
    }

}
