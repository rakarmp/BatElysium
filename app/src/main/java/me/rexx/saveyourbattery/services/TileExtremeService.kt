package me.rexx.saveyourbattery.services

import me.rexx.saveyourbattery.repositories.profiles.Profile

class TileExtremeService : ProfileTileService() {
    override val profile = Profile.EXTREME
}