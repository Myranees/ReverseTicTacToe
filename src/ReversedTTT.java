import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class ReversedTTT extends JFrame implements ActionListener {
    private final char[][] board;
    private char currentPlayer;
    private boolean gameEnd;
    private final Difficulty difficulty;
    private final JButton[][] buttons;

    enum Difficulty {
        EASY, MEDIUM, HARD
    }

    public ReversedTTT(int difficulty) {
        this.board = new char[3][3];
        this.currentPlayer = 'X';
        this.gameEnd = false;
        if (difficulty == 1)
            this.difficulty = Difficulty.EASY;
        else if (difficulty == 2)
            this.difficulty = Difficulty.MEDIUM;
        else
            this.difficulty = Difficulty.HARD;
        this.buttons = new JButton[3][3];
        initializeBoard();
        createGUI();
    }

    private void initializeBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = ' ';
            }
        }
    }

    private void createGUI() {
        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        boardPanel.setBackground(Color.BLACK);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton button = new JButton("");
                button.setFont(new Font("Ink Free", Font.BOLD, 60));
                button.setPreferredSize(new Dimension(200, 200));
                button.addActionListener(this);
                buttons[row][col] = button;
                boardPanel.add(button);
            }
        }

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        JLabel title = new JLabel("Reversed Tic Tac");
        title.setFont(new Font("Ink Free", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(JLabel.CENTER);

        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Ink Free", Font.BOLD, 24));
        newGameButton.addActionListener(this);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        titlePanel.add(title);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(newGameButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Reversed Tic-Tac-Toe");
        setResizable(false);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("") && !gameEnd && currentPlayer == 'X') {
                int row = -1, col = -1;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (button == buttons[i][j]) {
                            row = i;
                            col = j;
                            break;
                        }
                    }
                }
                if (row != -1 && col != -1) {
                    button.setText("X");
                    button.setForeground(Color.BLUE);
                    board[row][col] = 'X';

                    if (checkWin('X')) {
                        JOptionPane.showMessageDialog(this, "Player O wins!");
                        gameEnd = true;
                    } else if (!isBoardFull()) {
                        playComputerMove();
                        if (checkWin('O')) {
                            JOptionPane.showMessageDialog(this, "Player X wins!");
                            gameEnd = true;
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "It's a draw!");
                        gameEnd = true;
                    }
                }

            } else if (button.getText().equals("New Game")) {
                resetGame();
            }
        }
    }


    private void resetGame() {
        initializeBoard();
        currentPlayer = 'X';
        gameEnd = false;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
            }
        }
    }

    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void playComputerMove() {
        switch (difficulty) {
            case EASY:
                makeEasyMove();
                break;
            case MEDIUM:
                makeMediumMove();
                break;
            case HARD:
                makeHardMove();
                break;
        }
    }

    private void makeEasyMove() {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (board[row][col] != ' ');

        buttons[row][col].setText("O");
        buttons[row][col].setForeground(Color.RED);
        board[row][col] = 'O';
    }

    private void makeMediumMove() {
        // Check if the computer can win in the next move
        if (checkPossibleWinMove('O')) {
            return;
        }

        // Check if the player is about to win and block them
        if (checkPossibleWinMove('X')) {
            return;
        }

        // Play the best move using the Minimax algorithm
        makeHardMove();
    }

    private boolean checkPossibleWinMove(char player) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    // Simulate a move and check if it leads to a win
                    board[row][col] = player;
                    if (checkWin(player)) {
                        board[row][col] = 'O';
                        buttons[row][col].setText("O");
                        buttons[row][col].setForeground(Color.RED);
                        currentPlayer = 'X';
                        return true;
                    }
                    board[row][col] = ' ';
                }
            }
        }
        return false;
    }

    private void makeHardMove() {
        // Check if the board is empty
        boolean emptyBoard = true;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] != ' ') {
                    emptyBoard = false;
                    break;
                }
            }
            if (!emptyBoard) {
                break;
            }
        }

        if (emptyBoard) {
            // Play a random move in the center if the board is empty
            board[1][1] = 'O';
            buttons[1][1].setText("O");
            buttons[1][1].setForeground(Color.RED);
            currentPlayer = 'X';
            return;
        }

        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1;
        int bestCol = -1;

        // Look for the best move by trying all possible moves
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    // Simulate a move by the computer
                    board[row][col] = 'O';
                    int score = minimax(board, 0, false);
                    board[row][col] = ' ';

                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = row;
                        bestCol = col;
                    }
                }
            }
        }

        // Make the best move
        board[bestRow][bestCol] = 'O';
        buttons[bestRow][bestCol].setText("O");
        buttons[bestRow][bestCol].setForeground(Color.RED);
        currentPlayer = 'X';
    }

    private int minimax(char[][] board, int depth, boolean isMaximizing) {
        if (checkWin('O')) {
            return 1;
        } else if (checkWin('X')) {
            return -1;
        } else if (isBoardFull()) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board[row][col] == ' ') {
                        board[row][col] = 'O';
                        int score = minimax(board, depth + 1, false);
                        board[row][col] = ' ';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board[row][col] == ' ') {
                        board[row][col] = 'X';
                        int score = minimax(board, depth + 1, true);
                        board[row][col] = ' ';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }


    private boolean checkWin(char player) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == player && board[row][1] == player && board[row][2] == player) {
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == player && board[1][col] == player && board[2][col] == player) {
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        // Set the difficulty level (1: easy, 2: medium, 3: hard)
        int difficultyLevel = 2;
        ReversedTTT game = new ReversedTTT(difficultyLevel);
    }
}
