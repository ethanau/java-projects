
/**
 * A class for holding the information of the map, initialize the map, and check whether a position is traversable.
 *
 * @author Yebin Ge
 */
public class Map {

    // Some constants for representing the size of the map in default world.
    private final int WIDTH_DEFAULT = 6;
    private final int HEIGHT_DEFAULT = 4;

    // Some variables for representing the size of the map.
    private int width;
    private int height;

    // A two-dimensional array for holding the terrain of the map.
    private String[][] terrain;

    // Some constants for representing the terrain.
    private final String GROUND = ".";
    private final String MOUNTAIN = "#";
    private final String WATER = "~";

    /**
     * Initialize a default map.
     */
    public void initializeDefault() {

        // Set the width and height to default value.
        width = WIDTH_DEFAULT;
        height = HEIGHT_DEFAULT;
        terrain = new String[height][width];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                terrain[i][j] = GROUND;
            }
        }
    }

    /**
     * Initialize the map loaded from the game world file.
     * @param mapData an array holds the map data.
     */
    public void initialize(String[] mapData){

        terrain = new String[height][width];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){

                terrain[i][j] = mapData[i].substring(j, j + 1);
            }
        }
    }

    /**
     * Render the entity's symbol at a specific position in the game world.
     * @param y the row of the terrain array.
     * @param x the column of the terrain array.
     */
    public void display(int y, int x) {

        System.out.print(terrain[y][x]);
    }

    /**
     * Check whether the position in which the player or the monster is going to move is traversable.
     * @param positionY the positionY of the player or the monster.
     * @param positionX the positionX of the player or the monster.
     */
    public boolean isTraversable(int positionY, int positionX) {

        if (terrain[positionY][positionX].equals(GROUND)) {
            return true;
        }
        return false;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
