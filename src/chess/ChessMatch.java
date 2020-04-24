package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bispo;
import chess.pieces.Cavalo;
import chess.pieces.Peao;
import chess.pieces.Rainha;
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
			throw new ChessException("Voce n�o pode se colocar em xeque!");
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
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		board.placePiece(p, source);
		p.decreaseMoveCount();
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.therIsAPiece(position)) {
			throw new ChessException("Na h� pe�a na posicao inicial");
		}
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("A pe�a escolhida n�o � sua!");
		}
		if(!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("n�o h� movimentos possiveis para a peca escolhida!");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessException("a pe�a n�o pode se mover para esse destino!");
		}
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x ->((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {
			if(p instanceof Rei) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("N�o existe rei"+color+" no tabuleiro!");
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
		
		placeNewPiece('a',1,new Torre(board, Color.WHITE));
		placeNewPiece('b',1,new Cavalo(board, Color.WHITE));
		placeNewPiece('c',1,new Bispo(board, Color.WHITE));
		placeNewPiece('d',1,new Rainha(board, Color.WHITE));
		placeNewPiece('e',1,new Rei(board, Color.WHITE));
		placeNewPiece('f',1,new Bispo(board, Color.WHITE));
		placeNewPiece('g',1,new Cavalo(board, Color.WHITE));
		placeNewPiece('h',1,new Torre(board, Color.WHITE));
		placeNewPiece('a',2,new Peao(board,Color.WHITE));
		placeNewPiece('b',2,new Peao(board,Color.WHITE));
		placeNewPiece('c',2,new Peao(board,Color.WHITE));
		placeNewPiece('d',2,new Peao(board,Color.WHITE));
		placeNewPiece('e',2,new Peao(board,Color.WHITE));
		placeNewPiece('f',2,new Peao(board,Color.WHITE));
		placeNewPiece('g',2,new Peao(board,Color.WHITE));
		placeNewPiece('h',2,new Peao(board,Color.WHITE));
		
		
		placeNewPiece('a',8,new Torre(board, Color.BLACK));
		placeNewPiece('b',8,new Cavalo(board, Color.BLACK));
		placeNewPiece('c',8,new Bispo(board, Color.BLACK));
		placeNewPiece('d',8,new Rainha(board, Color.BLACK));
		placeNewPiece('e',8,new Rei(board, Color.BLACK));
		placeNewPiece('f',8,new Bispo(board, Color.BLACK));
		placeNewPiece('g',8,new Cavalo(board, Color.BLACK));
		placeNewPiece('h',8,new Torre(board, Color.BLACK));
		placeNewPiece('a',7,new Peao(board,Color.BLACK));
		placeNewPiece('b',7,new Peao(board,Color.BLACK));
		placeNewPiece('c',7,new Peao(board,Color.BLACK));
		placeNewPiece('d',7,new Peao(board,Color.BLACK));
		placeNewPiece('e',7,new Peao(board,Color.BLACK));
		placeNewPiece('f',7,new Peao(board,Color.BLACK));
		placeNewPiece('g',7,new Peao(board,Color.BLACK));
		placeNewPiece('h',7,new Peao(board,Color.BLACK));
		
	}

}
