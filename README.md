# SpigotGui
Gui helper for spigot

### Reading for GUI file.

After creating a GuiSetting you can get itam stacks or text from the GUi file by using one of the following functions:

- getItemStack(String name)
- getItemStack(String name, Map<String, String> replaceMap)
- getText(String name)
- getText(String name, Map<String, String> replaceMap)

## Easy to implement

### GUI Inventories
- KeyBoard
- NumberBoard
- GuiElementList
- List
  - PlayerList
  - GuiElementList
- Search

### GUI Elements
- Exit
- SimpleGuiElement
- ReturnElement

### Utils
- HeadUtil
  - ItemStack getPlayerHead(OfflinePlayer player)
  - ItemStack getTexturedHead(String texture, String displayName)
  - ItemStack getTexturedHead(String texture, String displayName, List<String> lore)

## Implementing your own Elements

To implement your own GuiElement extand from GuiElement and implement abstract methodes

## Implementing Custom inventories

If the provided inventories do not suit your needs you can also implement your own by either extending 'List' or 'InventoryUtil'
