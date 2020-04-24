package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Peao extends ChessPiece{
	
	private ChessMatch chessMatch;

	public Peao(Board board, Color color,ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0,0);
		
		if(getColor() == Color.WHITE) {
			p.setValues(position.getRow() - 1, position.getColumn());
			if(getBoard().positionExistis(p) && !getBoard().therIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() - 2, position.getColumn());
			Position p2 = new Position(position.getRow() - 1, position.getColumn());
			if(getBoard().positionExistis(p) && !getBoard().therIsAPiece(p) && getMoveCount() == 0 && getBoard().positionExistis(p2) && !getBoard().therIsAPiece(p2)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() - 1, position.getColumn()-1);
			if(getBoard().positionExistis(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			if(getBoard().positionExistis(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			/*MOVIMENTO EN PASSANT BRANCO*/
			if(position.getRow() == 3) {
				Position esquerda = new Position(position.getRow(), position.getColumn() - 1);
				if(getBoard().positionExistis(esquerda) && isThereOpponentPiece(esquerda) && getBoard().piece(esquerda) == chessMatch.getEnPassantVulnerable()) {
					mat[esquerda.getRow() - 1][esquerda.getColumn()] = true;
				}
				
				Position direta = new Position(position.getRow(), position.getColumn() + 1);
				if(getBoard().positionExistis(direta) && isThereOpponentPiece(direta) && getBoard().piece(direta) == chessMatch.getEnPassantVulnerable()) {
					mat[direta.getRow() - 1][direta.getColumn()] = true;
				}
			}
		}
		else {
			p.setValues(position.getRow() + 1, position.getColumn());
			if(getBoard().positionExistis(p) && !getBoard().therIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() + 2, position.getColumn());
			Position p2 = new Position(position.getRow() + 1, position.getColumn());
			if(getBoard().positionExistis(p) && !getBoard().therIsAPiece(p) && getMoveCount() == 0 && getBoard().positionExistis(p2) && !getBoard().therIsAPiece(p2)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() + 1, position.getColumn()-1);
			if(getBoard().positionExistis(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() + 1, position.getColumn() + 1);
			if(getBoard().positionExistis(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			/*MOVIMENTO EN PASSANT PRETA*/
			if(position.getRow() == 4) {
				Position esquerda = new Position(position.getRow(), position.getColumn() - 1);
				if(getBoard().positionExistis(esquerda) && isThereOpponentPiece(esquerda) && getBoard().piece(esquerda) == chessMatch.getEnPassantVulnerable()) {
					mat[esquerda.getRow() + 1][esquerda.getColumn()] = true;
				}
				
				Position direta = new Position(position.getRow(), position.getColumn() + 1);
				if(getBoard().positionExistis(direta) && isThereOpponentPiece(direta) && getBoard().piece(direta) == chessMatch.getEnPassantVulnerable()) {
					mat[direta.getRow() - 1][direta.getColumn()] = true;
				}
		}
	}
		
		return mat;
	}
	
	@Override
	public String toString() {
		return "P";
	}

}
