package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Rei;
import chess.pieces.Torre;

public class ChessMatch {
	
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	public ChessMatch() {
		board = new Board(8,8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for(int i=0; i<board.getRows();i++) {
			for(int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece)board.pieces(i, j);
			}
		}
		return mat;
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Voce não pode se colocar em xeque!");
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		if(testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}else {
			nextTurn();
		}
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		Piece p = board.removePiece(target);
		board.placePiece(p, source);
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.therIsAPiece(position)) {
			throw new ChessException("Na há peça na posicao inicial");
		}
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("A peça escolhida não é sua!");
		}
		if(!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("não há movimentos possiveis para a peca escolhida!");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessException("a peça não pode se mover para esse destino!");
		}
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x ->((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {
			if(p instanceof Rei) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("Não existe rei"+color+" no tabuleiro!");
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x ->((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for(Piece p : opponentPieces) {
			boolean[][] mat =p.possibleMoves();
			if((mat[kingPosition.getRow()][kingPosition.getColumn()])) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Color color) {
		if(!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x ->((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {
			boolean[][]mat = p.possibleMoves();
			for(int i=0; i<board.getRows(); i++) {
				for(int j=0; j<board.getColumns();j++) {
					if(mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i,j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source,target,capturedPiece);
						if(!testCheck) {
							return false;
						}
					}
				}
			}
		}return true;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
		
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.WHITE : Color.WHITE;
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private void initialSetup() {
		
		placeNewPiece('h', 7,new Torre(board, Color.WHITE));
		placeNewPiece('d', 1,new Torre(board, Color.WHITE));
		placeNewPiece('e', 1,new Rei(board, Color.WHITE));
		
		placeNewPiece('b', 8,new Torre(board, Color.BLACK));
		placeNewPiece('a', 8,new Rei(board, Color.BLACK));
		
	}

}
