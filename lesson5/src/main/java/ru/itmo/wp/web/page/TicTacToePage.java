package ru.itmo.wp.web.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {
    public enum Cell {X, O}

    public static class State {
        private final int SIZE = 3;
        private boolean crossesMove;
        private String phase;
        private int nonemptyCellsCount;
        private final Cell[][] cells;

        State() {
            nonemptyCellsCount = 0;
            crossesMove = true;
            phase = "RUNNING";
            cells = new Cell[SIZE][SIZE];
        }

        public boolean getCrossesMove() {
            return crossesMove;
        }

        public int getSize() {
            return SIZE;
        }

        public Cell[][] getCells() {
            return cells;
        }

        public String getPhase() {
            return phase;
        }

        public void makeTurn(int row, int col) {
            if (row >= SIZE || col >=SIZE
                    || cells[row][col] != null || !phase.equals("RUNNING")) {
                return;
            }
            Cell cell;
            if (crossesMove) {
                cell = Cell.X;
            } else {
                cell = Cell.O;
            }
            cells[row][col] = cell;
            boolean isRowWin = true, isColWin = true, isDiag1Win = true, isDiag2Win = true;
            for (int i = 0; i < SIZE; ++i) {
                if (cells[row][i] != cell) {
                    isRowWin = false;
                }
                if (cells[i][col] != cell) {
                    isColWin = false;
                }
                if (cells[i][i] != cell) {
                    isDiag1Win = false;
                }
                if (cells[SIZE - 1 - i][i] != cell) {
                    isDiag2Win = false;
                }
            }
            nonemptyCellsCount++;
            if (isColWin || isDiag1Win || isDiag2Win || isRowWin) {
                if (crossesMove) {
                    phase = "WON_X";
                } else {
                    phase = "WON_O";
                }
            } else if (nonemptyCellsCount == SIZE * SIZE) {
                phase = "DRAW";
            }
            crossesMove = !crossesMove;
        }
    }

    private void action(HttpServletRequest request, Map<String, Object> map) {
        HttpSession session = request.getSession();
        State state = (State) session.getAttribute("state");
        if (state == null) {
            state = new State();
            session.setAttribute("state", state);
        }
        map.put("state", state);
    }

    private void newGame(HttpServletRequest request, Map<String, Object> map) {
        HttpSession session = request.getSession();
        State state = new State();
        session.setAttribute("state", state);
        map.put("state", state);
    }

        private void onMove(HttpServletRequest request, Map<String, Object> map) {
        HttpSession session = request.getSession();
        Enumeration<String> parameters = request.getParameterNames();
        int row = 0, col = 0;
        final String cellString = "cell_";
        while (parameters.hasMoreElements()) {
            String st = parameters.nextElement();
            if (st.startsWith(cellString)) {
                row = st.charAt(cellString.length()) - '0';
                col = st.charAt(cellString.length() + 1) - '0';
            }
        }
        State state = (State) session.getAttribute("state");
        if (state == null) {
            state = new State();
        }
        state.makeTurn(row, col);
        session.setAttribute("state", state);
        map.put("state", state);
    }


}
