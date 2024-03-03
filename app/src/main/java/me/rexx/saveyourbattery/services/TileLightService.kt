package me.rexx.saveyourbattery.services

import me.rexx.saveyourbattery.repositories.profiles.Profile

class TileLightService : ProfileTileService() {
    override val profile = Profile.LIGHT
}