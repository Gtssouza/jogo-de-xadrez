package boardgame;

public class Board {
	
	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	public Board(int rows, int columns) {
		if(rows<1 || columns<1) {
			throw new BoardException("É necessario que o tabuleiro tenha mais de 1 linha e 1 coluna");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	public Piece pieces(int row, int column) {
		if(!positionExistis(row,column)) {
			throw new BoardException("Esta posicao nao esta no tabuleiro");
		}
		return pieces[row][column];
	}
	
	public Piece piece(Position position) {
		if(!positionExistis(position)) {
			throw new BoardException("Esta posicao nao esta no tabuleiro");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) {
		if(therIsAPiece(position)) {
			throw new BoardException("Ja possui uma peca nessa posicao"+position);
		}
		pieces[position.getRow()][position.getColumn()]=piece;
		piece.position=position;
	}
	
	public Piece removePiece(Position position) {
		if(!positionExistis(position)) {
			throw new BoardException("Posicao nao existe no tabuleiro");
		}if(piece(position) == null) {
			return null;
		}Piece aux = piece(position);
		aux.position = null;
		pieces[position.getRow()][position.getColumn()] = null;
		return aux;
	}
	
	private boolean positionExistis(int row, int column) {
		return row >= 0 && row < rows && column >= 0 && column < columns;
	}
	
	public boolean positionExistis(Position position) {
		return positionExistis(position.getRow(), position.getColumn());
	}
	
	public boolean therIsAPiece(Position position) {
		if(!positionExistis(position)) {
			throw new BoardException("Esta posicao nao esta no tabuleiro");
		}
		return piece(position) != null ;
	}

}
