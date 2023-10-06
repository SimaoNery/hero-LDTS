import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Arena {
    private int width;
    private int height;
    private Hero hero;
    private List<Wall> walls;

    public int get_Width(){
        return this.width;
    }
    public int get_Height(){
        return this.height;
    }
    public void set_Width(int width){
        this.width = width;
    }
    public void set_Height(int height){
        this.height = height;
    }

    public Arena(int width, int height){
        this.width = width;
        this.height = height;
        this.hero = new Hero(width/2, height/2);
        this.walls = createWalls();
    }
    private List<Wall> createWalls() {
        List<Wall> walls = new ArrayList<>();
        for (int c = 0; c < width; c++) {
            walls.add(new Wall(c, 0));
            walls.add(new Wall(c, height - 1));
        }
        for (int r = 1; r < height - 1; r++) {
            walls.add(new Wall(0, r));
            walls.add(new Wall(width - 1, r));
        }
        return walls;
    }

    public void draw(TextGraphics graphics){
        for(Wall wall : walls){
            wall.draw(graphics);
        }
        hero.draw(graphics);
    }

    public boolean canHeroMove(Position position) {
        int newX = position.getX();
        int newY = position.getY();
        if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
            for (Wall wall : walls) {
                if (wall.getPosition().equals(position)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }
    public void moveHero(Position position) {
        if (canHeroMove(position)) {
            hero.setPosition(position);
        }
    }
    public void processKey(KeyStroke key){
        System.out.println(key);
        switch(key.getKeyType()) {
            case ArrowUp:
                moveHero(hero.moveUp());
                break;
            case ArrowDown:
                moveHero(hero.moveDown());
                break;
            case ArrowLeft:
                moveHero(hero.moveLeft());
                break;
            case ArrowRight:
                moveHero(hero.moveRight());
                break;
            case Character:
                if (key.getCharacter() == 'q') {
                        System.exit(0);
                    }
                break;
            default:
                break;
        }
    }

}
