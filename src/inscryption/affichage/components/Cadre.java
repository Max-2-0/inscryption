package inscryption.affichage.components;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Cadre {
    private int m_x;
    private int m_y;
    private int m_height;
    private int m_width;

    public Cadre(int x, int y, int height, int width) {
        this.m_x = x;
        this.m_y = y;
        this.m_height = height;
        this.m_width = width;
    }

    public void draw(TextGraphics tg, boolean points) {
        char horizontal = points ? '·' : '─';
        char vertical = points ? '·' : '│';

        String top = buildLine('╭', horizontal, '╮');
        String middle = buildLine(vertical, ' ', vertical);
        String bottom = buildLine('╰', horizontal, '╯');

        tg.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        tg.putString(m_x, m_y, top);
        for (int i = 1; i < m_height - 1; i++) {
            tg.putString(m_x, m_y + i, middle);
        }
        tg.putString(m_x, m_y + m_height - 1, bottom);
        tg.setForegroundColor(TextColor.ANSI.WHITE);
    }

    private String buildLine(char left, char fill, char right) {
        StringBuilder sb = new StringBuilder();
        sb.append(left);
        for (int i = 0; i < m_width - 2; i++) sb.append(fill);
        sb.append(right);
        return sb.toString();
    }

    public void drawSeparation(TextGraphics tg, int line) {
        StringBuilder sb = new StringBuilder();
        sb.append('├');
        for (int i = 0; i < m_width - 2; i++) sb.append('─');
        sb.append('┤');

        tg.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        tg.putString(m_x, m_y + line, sb.toString());
        tg.setForegroundColor(TextColor.ANSI.WHITE);
    }

    public void drawTwoFromLeft(TextGraphics tg) {
        tg.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        tg.putString(m_x, m_y, "╭─");
        for (int i = 1; i < m_height - 1; i++) tg.putString(m_x, m_y + i, "│ ");
        tg.putString(m_x, m_y + m_height - 1, "╰─");
        tg.setForegroundColor(TextColor.ANSI.WHITE);
    }

    public void drawTwoFromRight(TextGraphics tg) {
        tg.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        tg.putString(m_x, m_y, "─╮");
        for (int i = 1; i < m_height - 1; i++) tg.putString(m_x, m_y + i, " │");
        tg.putString(m_x, m_y + m_height - 1, "─╯");
        tg.setForegroundColor(TextColor.ANSI.WHITE);
    }

    public void updateX(int newX) { this.m_x = newX; }
    public void updateY(int newY) { this.m_y = newY; }

    @Override
    public String toString() {
        return "Cadre[x=" + m_x + " y=" + m_y + " w=" + m_width + " h=" + m_height + "]";
    }
}
