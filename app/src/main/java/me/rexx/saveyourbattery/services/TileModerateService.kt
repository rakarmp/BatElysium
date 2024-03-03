package me.rexx.saveyourbattery.services

import me.rexx.saveyourbattery.repositories.profiles.Profile

class TileModerateService : ProfileTileService() {
    override val profile = Profile.MODERATE
}