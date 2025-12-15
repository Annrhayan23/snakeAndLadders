import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.HashMap;

public class SnakeAndLadders extends JFrame {
    private int player1Pos = 1, player2Pos = 1;
    private boolean player1Turn = true;
    private JLabel diceLabel;
    private JButton rollButton;
    private BoardPanel boardPanel;
    private Random rand = new Random();

    // Snakes and Ladders mapping
    private HashMap<Integer, Integer> snakes = new HashMap<>();
    private HashMap<Integer, Integer> ladders = new HashMap<>();

    public SnakeAndLadders() {
        setTitle("Advanced Snake and Ladders");
        setSize(700, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        diceLabel = new JLabel("Roll the dice!", SwingConstants.CENTER);
        diceLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(diceLabel, BorderLayout.NORTH);

        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        rollButton = new JButton("Roll Dice");
        add(rollButton, BorderLayout.SOUTH);

        initializeSnakesAndLadders();

        rollButton.addActionListener(e -> rollDice());

        setVisible(true);
    }

    private void initializeSnakesAndLadders() {
        // Ladders
        ladders.put(4, 14);
        ladders.put(9, 31);
        ladders.put(20, 38);
        ladders.put(28, 84);
        ladders.put(40, 59);
        ladders.put(63, 81);
        ladders.put(71, 91);

        // Snakes
        snakes.put(17, 7);
        snakes.put(54, 34);
        snakes.put(62, 19);
        snakes.put(64, 60);
        snakes.put(87, 24);
        snakes.put(93, 73);
        snakes.put(95, 75);
        snakes.put(99, 78);
    }

    private void rollDice() {
        int dice = rand.nextInt(6) + 1;
        diceLabel.setText("Dice Rolled: " + dice);

        if(player1Turn) {
            animateMovement(1, dice);
        } else {
            animateMovement(2, dice);
        }
    }

    private void animateMovement(int player, int dice) {
        new Thread(() -> {
            try {
                for(int i = 0; i < dice; i++) {
                    Thread.sleep(200); // animation speed
                    if(player == 1) {
                        player1Pos++;
                        if(player1Pos > 100) player1Pos--;
                    } else {
                        player2Pos++;
                        if(player2Pos > 100) player2Pos--;
                    }
                    boardPanel.repaint();
                }

                // Check for snakes or ladders
                if(player == 1 && ladders.containsKey(player1Pos)) {
                    JOptionPane.showMessageDialog(this, "Ladder! Climb up!");
                    player1Pos = ladders.get(player1Pos);
                } else if(player == 1 && snakes.containsKey(player1Pos)) {
                    JOptionPane.showMessageDialog(this, "Snake! Slide down!");
                    player1Pos = snakes.get(player1Pos);
                }

                if(player == 2 && ladders.containsKey(player2Pos)) {
                    JOptionPane.showMessageDialog(this, "Ladder! Climb up!");
                    player2Pos = ladders.get(player2Pos);
                } else if(player == 2 && snakes.containsKey(player2Pos)) {
                    JOptionPane.showMessageDialog(this, "Snake! Slide down!");
                    player2Pos = snakes.get(player2Pos);
                }

                boardPanel.repaint();

                if(player1Pos == 100) {
                    JOptionPane.showMessageDialog(this, "Player 1 Wins! 🎉");
                    resetGame();
                    return;
                }
                if(player2Pos == 100) {
                    JOptionPane.showMessageDialog(this, "Player 2 Wins! 🎉");
                    resetGame();
                    return;
                }

                player1Turn = !player1Turn;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void resetGame() {
        player1Pos = 1;
        player2Pos = 1;
        player1Turn = true;
        boardPanel.repaint();
        diceLabel.setText("Roll the dice!");
    }

    class BoardPanel extends JPanel {
        int size = 60;

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw 10x10 board
            for(int i = 0; i < 10; i++) {
                for(int j = 0; j < 10; j++) {
                    int x = j * size;
                    int y = (9 - i) * size;
                    g.setColor((i + j) % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                    g.fillRect(x, y, size, size);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, size, size);

                    int cellNum = i * 10 + j + 1;
                    g.drawString(String.valueOf(cellNum), x + 5, y + 15);
                }
            }

            // Draw ladders
            g.setColor(Color.GREEN.darker());
            for(int start : ladders.keySet()) {
                Point p1 = getCellCenter(start);
                Point p2 = getCellCenter(ladders.get(start));
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

            // Draw snakes
            g.setColor(Color.RED.darker());
            for(int start : snakes.keySet()) {
                Point p1 = getCellCenter(start);
                Point p2 = getCellCenter(snakes.get(start));
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

            // Draw players
            drawPlayer(g, player1Pos, Color.BLUE);
            drawPlayer(g, player2Pos, Color.MAGENTA);
        }

        private Point getCellCenter(int pos) {
            int row = (pos - 1) / 10;
            int col = (pos - 1) % 10;
            if(row % 2 == 1) col = 9 - col;
            int x = col * size + size / 2;
            int y = (9 - row) * size + size / 2;
            return new Point(x, y);
        }

        private void drawPlayer(Graphics g, int pos, Color color) {
            Point p = getCellCenter(pos);
            g.setColor(color);
            g.fillOval(p.x - 15, p.y - 15, 30, 30);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SnakeAndLadders());
    }
}
