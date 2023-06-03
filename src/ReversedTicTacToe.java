import java.util.*;

public class ReversedTicTacToe {
    private char[][] board;
    private char currentPlayer;
    private char playerSymbol;
    private boolean gameEnd;
    private Difficulty difficulty;
    private List<ReversedTicTacToe> savedGames;

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    public ReversedTicTacToe(Difficulty difficulty) {
        board = new char[3][3];
        currentPlayer = 'X';
        gameEnd = false;
        this.difficulty = difficulty;
        savedGames = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    private void printBoard() {
        System.out.println("---------");
        for (int row = 0; row < 3; row++) {
            System.out.print("| ");
            for (int col = 0; col < 3; col++) {
                System.out.print(board[row][col] + " | ");
            }
            System.out.println("\n---------");
        }
    }

    private void makeMove(int row, int col) {
        if (board[row][col] == ' ') {
            board[row][col] = currentPlayer;
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        } else {
            System.out.println("Invalid move. The selected position is already occupied.");
        }
    }

    private void checkGameEnd() {
        if (checkWin('X')) {
            gameEnd = true;
            System.out.println("Player X wins!");
        } else if (checkWin('O')) {
            gameEnd = true;
            System.out.println("Player O wins!");
        } else if (isBoardFull()) {
            gameEnd = true;
            System.out.println("It's a draw!");
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

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void makePlayerMove() {
        Scanner scanner = new Scanner(System.in);
        int row, col;
        do {
            System.out.print("Enter your move (row [0-2] and column [0-2]): ");
            row = scanner.nextInt();
            col = scanner.nextInt();
        } while (row < 0 || row > 2 || col < 0 || col > 2);
        makeMove(row, col);
    }

    private void makeAIMove() {
        switch (difficulty) {
            case EASY:
                makeRandomMove();
                break;
            case MEDIUM:
                makeSlightlyOptimalMove();
                break;
            case HARD:
                makeMostlyOptimalMove();
                break;
        }
    }

    private void makeRandomMove() {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (board[row][col] != ' ');
        makeMove(row, col);
    }

    private void makeSlightlyOptimalMove() {
        // Check if there is a winning move available for the AI
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    // Make a move and check if it results in a win
                    board[row][col] = currentPlayer;
                    if (checkWin(currentPlayer)) {
                        return;
                    }
                    // Undo the move
                    board[row][col] = ' ';
                }
            }
        }

        // If there is no winning move, make a random move
        makeRandomMove();
    }

    private void makeMostlyOptimalMove() {
        // Check if there is a winning move available for the AI
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    // Make a move and check if it results in a win
                    board[row][col] = currentPlayer;
                    if (checkWin(currentPlayer)) {
                        return;
                    }
                    // Undo the move
                    board[row][col] = ' ';
                }
            }
        }

        // Check if there is a winning move available for the player and block it
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    // Make a move and check if it blocks the player's win
                    board[row][col] = playerSymbol;
                    if (checkWin(playerSymbol)) {
                        board[row][col] = currentPlayer; // Block the player's win
                        return;
                    }
                    // Undo the move
                    board[row][col] = ' ';
                }
            }
        }

        // If there are no winning moves, make a random move
        makeRandomMove();
    }

    public void playGame() {
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;
        int option;

        do {
            System.out.println("1. Start New Game");
            System.out.println("2. Load Saved Game");
            System.out.print("Select an option: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    validInput = true;
                    break;
                case 2:
                    validInput = true;
                    loadGame();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } while (!validInput);

        if (option == 1) {
            System.out.println("Starting a new game.");
            while (!gameEnd) {
                printBoard();
                makePlayerMove();
                checkGameEnd();
                if (!gameEnd) {
                    makeAIMove();
                    checkGameEnd();
                }
            }
        }
    }

    private void loadGame() {
        Scanner scanner = new Scanner(System.in);
        if (savedGames.isEmpty()) {
            System.out.println("No saved games available.");
            return;
        }

        System.out.println("Saved games:");
        for (int i = 0; i < savedGames.size(); i++) {
            System.out.println((i + 1) + ". " + savedGames.get(i).getName());
        }

        int selection;
        do {
            System.out.print("Select a game to load (1-" + savedGames.size() + "): ");
            selection = scanner.nextInt();
        } while (selection < 1 || selection > savedGames.size());

        ReversedTicTacToe gameToLoad = savedGames.get(selection - 1);
        this.board = gameToLoad.board;
        this.currentPlayer = gameToLoad.currentPlayer;
        this.gameEnd = gameToLoad.gameEnd;
        this.difficulty = gameToLoad.difficulty;

        System.out.println("Game loaded successfully.");
    }

    private String getName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a name for the game: ");
        return scanner.nextLine();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Reversed Tic Tac Toe!");

        Difficulty difficulty = null;
        boolean validInput = false;

        do {
            System.out.println("Choose the AI difficulty level:");
            System.out.println("1. Easy");
            System.out.println("2. Medium");
            System.out.println("3. Hard");
            System.out.print("Select an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    validInput = true;
                    difficulty = Difficulty.EASY;
                    break;
                case 2:
                    validInput = true;
                    difficulty = Difficulty.MEDIUM;
                    break;
                case 3:
                    validInput = true;
                    difficulty = Difficulty.HARD;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } while (!validInput);

        ReversedTicTacToe game = new ReversedTicTacToe(difficulty);
        game.playGame();
    }
}
