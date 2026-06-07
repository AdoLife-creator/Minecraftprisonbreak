public static void setPrisoner(Player player) {

    guard.removeEntry(player.getName());
    prisoner.addEntry(player.getName());

    cache.put(player.getUniqueId(), TeamType.PRISONER);

    player.teleport(SpawnManager.get("spawns.mahkum"));
}

public static void setGuard(Player player) {

    prisoner.removeEntry(player.getName());
    guard.addEntry(player.getName());

    cache.put(player.getUniqueId(), TeamType.GUARD);

    player.teleport(SpawnManager.get("spawns.gardiyan"));
}
