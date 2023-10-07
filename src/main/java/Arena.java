import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {
    private int width;
    private int height;
    private Hero hero;
    private List<Wall> walls;
    private List<Coin> coins;
    private List<Monster> monsters;


    public Arena(int width, int height){
        this.width = width;
        this.height = height;
        this.hero = new Hero(width/2, height/2);
        this.walls = createWalls();
        this.coins = createCoins();
        this.monsters = createMonsters();
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
    private List<Coin> createCoins(){
        Random random = new Random();
        ArrayList<Coin> Coins = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            int coinX, coinY;
            do {
                coinX = random.nextInt(width - 2) + 1;
                coinY = random.nextInt(height - 2) + 1;
            }
            while (isOverlappingWithHero(coinX, coinY) || isOverlappingWithWall(coinX, coinY));
            Coins.add(new Coin(coinX, coinY));
        }

        return Coins;
    }

    private List<Monster> createMonsters() {
        Random random = new Random();
        ArrayList<Monster> generatedMonsters = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            int monsterX, monsterY;
            do {
                monsterX = random.nextInt(width - 2) + 1;
                monsterY = random.nextInt(height - 2) + 1;
            } while (isOverlappingWithHero(monsterX, monsterY) || isOverlappingWithWall(monsterX, monsterY));

            generatedMonsters.add(new Monster(monsterX, monsterY));
        }

        return generatedMonsters;
    }


    private boolean isOverlappingWithHero(int x, int y) {
        return hero.getPosition().getX() == x && hero.getPosition().getY() == y;
    }

    private boolean isOverlappingWithWall(int x, int y) {
        for (Wall wall : walls) {
            if (wall.getPosition().getX() == x && wall.getPosition().getY() == y) {
                return true;
            }
        }
        return false;
    }


    public void draw(TextGraphics graphics){
        for(Wall wall : walls){
            wall.draw(graphics);
        }
        for (Coin coin : coins) {
            coin.draw(graphics);
        }
        for (Monster monster : monsters) {
            monster.draw(graphics);
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

    public void retrieveCoins() {
        Position heroPosition = hero.getPosition();
        for (int i = 0; i < coins.size(); i++) {
            Coin coin = coins.get(i);
            if (heroPosition.equals(coin.getPosition())) {
                coins.remove(i);
                break;
            }
        }
    }


    public void moveMonsters() {
        for (Monster monster : monsters) {
            Position newPosition = monster.move();
            if (canMonsterMove(newPosition)) {
                monster.setPosition(newPosition);
            }
        }
    }

    public void verifyMonsterCollisions() {
        for (Monster monster : monsters) {
            if (hero.getPosition().equals(monster.getPosition())) {
                System.out.println("Game over! The hero touched a monster.");
                System.exit(0);
            }
        }
    }

    public boolean canMonsterMove(Position position) {
        int newX = position.getX();
        int newY = position.getY();
        if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
            for (Wall wall : walls) {
                if (wall.getPosition().equals(position)) {
                    return false;
                }
            }
            for (Monster monster : monsters) {
                if (monster.getPosition().equals(position)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
