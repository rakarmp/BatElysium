package me.rexx.saveyourbattery.services

import me.rexx.saveyourbattery.repositories.profiles.Profile

class TileDefaultService : ProfileTileService() {
    override val profile = Profile.DEFAULT
}