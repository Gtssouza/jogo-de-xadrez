package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rainha extends ChessPiece {

	public Rainha(Board board, Color color) {
		super(board, color);

	}

	@Override
	public String toString() {
		return "Q";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);

		// posicao acima da pe�a
		p.setValues(position.getRow() - 1, position.getColumn());
		while (getBoard().positionExistis(p) && !getBoard().therIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() - 1);
		}
		if (getBoard().positionExistis(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// posicao esquerda da pe�a
		p.setValues(position.getRow(), position.getColumn() - 1);
		while (getBoard().positionExistis(p) && !getBoard().therIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() - 1);
			;
		}
		if (getBoard().positionExistis(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// posicao direita da pe�a
		p.setValues(position.getRow(), position.getColumn() + 1);
		while (getBoard().positionExistis(p) && !getBoard().therIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() + 1);
			;
		}
		if (getBoard().positionExistis(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// posi��o abaixo da pe�a
		p.setValues(position.getRow() + 1, position.getColumn());
		while (getBoard().positionExistis(p) && !getBoard().therIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1);
		}
		if (getBoard().positionExistis(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// posicao superior esquerda
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		while (getBoard().positionExistis(p) && !getBoard().therIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() - 1);
		}
		if (getBoard().positionExistis(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// posicao superior direita
		p.setValues(position.getRow() - 1, position.getColumn() + 1);
		while (getBoard().positionExistis(p) && !getBoard().therIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() + 1);
			;
		}
		if (getBoard().positionExistis(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// posicao inferior direita
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		while (getBoard().positionExistis(p) && !getBoard().therIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() + 1);
			;
		}
		if (getBoard().positionExistis(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// posi��o inferior esquerda
		p.setValues(position.getRow() + 1, position.getColumn() - 1);
		while (getBoard().positionExistis(p) && !getBoard().therIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() - 1);
		}
		if (getBoard().positionExistis(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		return mat;
	}

}
