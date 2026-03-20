import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private final int width;
    private final int height;
    private final List<Entity> entities;
    private final Map<Position, List<Entity>> entityMap;

    private int viewportStartX;
    private int viewportWidth;
    private int cellRenderWidth;

    public Board(int width, int height) {
        if (width <= 0)
            throw new IllegalArgumentException("Width must be positive.");
        if (height <= 0)
            throw new IllegalArgumentException("Height must be positive.");
        this.width = width;
        this.height = height;
        this.entities = new ArrayList<>();
        this.entityMap = new HashMap<>();
        this.viewportStartX = 0;
        this.viewportWidth = width;
        this.cellRenderWidth = 3;
    }

    public int getWidth() {return width;}

    public int getHeight() {return height;}

    public int getViewportStartX() {return viewportStartX;}

    public int getViewportWidth() {return viewportWidth;}

    public int getCellRenderWidth() {return cellRenderWidth;}

    public void setViewportWidth(int viewportWidth) {
        if (viewportWidth <= 0)
            throw new IllegalArgumentException("Viewport width must be positive.");
        this.viewportWidth = Math.min(viewportWidth, width);
        clampViewport();
    }

    public void setCellRenderWidth(int cellRenderWidth) {
        if (cellRenderWidth <= 0)
            throw new IllegalArgumentException("Cell render width must be positive.");
        this.cellRenderWidth = cellRenderWidth;
    }

    public List<Entity> getEntities() {return new ArrayList<>(entities);}

    public void addEntity(Entity entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity cannot be null.");
        if (!isValidMove(entity.getPosition()))
            throw new IllegalArgumentException("Entity position is out of bounds: " + entity.getPosition());
        entities.add(entity);
        rebuildEntityMap();
    }

    public void removeEntity(Entity entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity cannot be null.");
        entities.remove(entity);
        rebuildEntityMap();
    }

    public void clearEntities() {
        entities.clear();
        entityMap.clear();
    }

    public void rebuildEntityMap() {
        entityMap.clear();
        for (Entity entity : entities) {
            Position position = entity.getPosition();
            entityMap.computeIfAbsent(position, k -> new ArrayList<>()).add(entity);
        }
    }

    public List<Entity> getEntitiesAt(Position position) {
        if (position == null)
            throw new IllegalArgumentException("Position cannot be null.");
        List<Entity> found = entityMap.get(position);
        if (found == null)
            return new ArrayList<>();
        return new ArrayList<>(found);
    }

    public void updatePosition(Entity entity, Position newPosition) {
        if (entity == null)
            throw new IllegalArgumentException("Entity cannot be null.");
        if (newPosition == null)
            throw new IllegalArgumentException("New position cannot be null.");
        if (!entities.contains(entity))
            throw new IllegalArgumentException("Entity is not on this board.");
        if (!isValidMove(newPosition))
            throw new IllegalArgumentException("Position is out of bounds: " + newPosition);
        entity.move(newPosition);
        rebuildEntityMap();
    }

    public boolean isValidMove(Position position) {
        if (position == null)
            return false;
        return position.getX() >= 0
                && position.getX() < width
                && position.getY() >= 0
                && position.getY() < height;
    }

    public void shiftViewportLeft() {
        if (viewportStartX > 0)
            viewportStartX--;
    }

    public void shiftViewportRight() {
        if (viewportStartX + viewportWidth < width)
            viewportStartX++;
    }

    private void clampViewport() {
        if (viewportStartX < 0)
            viewportStartX = 0;
        int maxStart = Math.max(0, width - viewportWidth);
        if (viewportStartX > maxStart)
            viewportStartX = maxStart;
    }

    public void render() {
        int endX = Math.min(width, viewportStartX + viewportWidth);
        for (int y = 0; y < height; y++) {
            StringBuilder row = new StringBuilder();
            for (int x = viewportStartX; x < endX; x++) {
                Position position = new Position(x, y);
                List<Entity> entitiesAtPosition = getEntitiesAt(position);
                String cellSymbol = ".";
                if (!entitiesAtPosition.isEmpty())
                    cellSymbol = entitiesAtPosition.get(0).getSymbol();
                row.append(padCell(cellSymbol));
            }
            System.out.println(row);
        }
    }

    private String padCell(String content) {
        if (content == null)
            content = "";
        if (content.length() >= cellRenderWidth)
            return content;
        StringBuilder padded = new StringBuilder(content);
        while (padded.length() < cellRenderWidth)
            padded.append(" ");
        return padded.toString();
    }
}
