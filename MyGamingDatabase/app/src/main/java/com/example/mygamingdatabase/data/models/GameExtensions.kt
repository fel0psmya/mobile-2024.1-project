package com.example.mygamingdatabase.data.models

import com.example.mygamingdatabase.data.DTOs.GameDTO

fun Game.toDTO(): GameDTO {
    return GameDTO(
        id = this.id,
        isFavorite = this.isFavorite.value,
        isAddedToList = this.isAddedToList.value,
        isReminded = this.isReminded.value,
        reminderTime = this.reminderTime.value,
        reminderDays = this.reminderDays.value,
        userScore = this.userScore,
        status = this.status
    )
}