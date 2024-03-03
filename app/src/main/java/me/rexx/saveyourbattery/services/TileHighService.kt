package me.rexx.saveyourbattery.services

import me.rexx.saveyourbattery.repositories.profiles.Profile

class TileHighService : ProfileTileService() {
    override val profile = Profile.HIGH
}