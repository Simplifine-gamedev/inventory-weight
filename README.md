# Inventory Weight

A Minecraft Fabric mod that makes your inventory weight matter! Your movement speed is affected by what you're carrying - travel light and fast, or carry more but move slower.

## Features

- **Dynamic Movement Speed**: Your walking and sprinting speed scales based on total inventory weight
- **Item Weight Categories**: Different items have different weights:
  - **Very Light** (0.1): Paper, maps, books
  - **Light** (0.3): Food, feathers, flowers, seeds, plants
  - **Medium** (1.0): Wood (logs, planks), most tools and items
  - **Heavy** (2.0): Stone, cobblestone, ores, metal ingots, bricks
  - **Very Heavy** (5.0): Shulker boxes, anvils, enchanting tables, beacons
- **Smooth Scaling**: Speed ranges from 100% (empty inventory) to 60% (maximum load)
- **HUD Display**: Shows your current speed percentage and weight load in the top-left corner
- **Non-Punishing Design**: Uses a square-root curve so light loads barely affect your speed

## How It Works

The mod calculates the total weight of all items in your inventory (main inventory, hotbar, and offhand). Based on this weight, a speed modifier is applied that smoothly reduces your movement speed.

### Weight Formula
- Total weight = sum of (item count x item weight) for all items
- Speed multiplier uses a smooth curve: lighter loads barely affect you, heavier loads slow you down more
- Minimum speed is 60% even when fully loaded

### HUD Display
The top-left corner shows:
- **Speed percentage**: Your current movement speed (green = fast, yellow = moderate, red = slow)
- **Weight percentage**: How much of your theoretical max weight you're carrying

## Requirements

- Minecraft 1.21.1
- Fabric Loader 0.16.0+
- Fabric API

## Installation

1. Install Fabric Loader for Minecraft 1.21.1
2. Download Fabric API and place in your mods folder
3. Download `inventory-weight-1.0.0.jar` and place in your mods folder
4. Launch Minecraft with the Fabric profile

## Building from Source

```bash
git clone https://github.com/Simplifine-gamedev/inventory-weight.git
cd inventory-weight
./gradlew build
```

The built JAR will be in `build/libs/`.

## Gameplay Tips

- **Mining trips**: Bring only essential tools, leave space for ore
- **Base building**: Accept the slowdown when hauling materials
- **Exploration**: Travel light for speed, use shulker boxes strategically
- **Combat**: Being loaded down makes you slower - drop heavy items before a fight

## Multiplayer

The mod works on multiplayer servers! Speed modifications are applied server-side using Minecraft's attribute system, ensuring fair and consistent gameplay for all players.

## License

MIT License - Feel free to include in modpacks!
