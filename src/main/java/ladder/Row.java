package ladder;
// 문제점
// drawLine, nextPosition 등의 하드코딩 (+1 or -1)

import static ladder.Direction.*;

// 사다리의 하나의 가로줄 (행)
public class Row {
    // todo Node 클래스로 분리(row 배열에서 값을 하나씩 꺼내서 볼 필요가 없다..?)
    Node[] nodes;

    public Row(GreaterThanOne numberOfPerson) {
        nodes = new Node[numberOfPerson.getNumber()]; // 노드 배열개수할당
        for (int i = 0; i < numberOfPerson.getNumber(); i++) {
            nodes[i] = Node.from(NONE);
        }
    }

    // todo : 상수 리팩토링 (1,-1, 0)
    public void drawLine(Position startPosition) {
        validateDrawLinePosition(startPosition); // 선 그리기 유효성 검사 통합

        setDirectionBetweenNextPosition(startPosition);
    }

    private void setDirectionBetweenNextPosition(Position position) {
        nodes[position.getPosition()].setRightNode(position); // 현재 노드에 1대입 (오른쪽 이동)
        position.next();
        nodes[position.getPosition()].setLeftNode(position); // 그 다음 노드에 -1대입 (왼쪽 이동)
    }

    // run 메소드의 다음 상태
    public void nextPosition(Position position) {
        validatePosition(position); // 1-2. next, prev 이전에 다시 position 검증
        // nextPosition method return type : Position -> void (1)

        nodes[position.getPosition()].move(position);
    }

    // Todo 상수 하드코딩도 enum으로 클래스단위로 관리하자

    private void validatePosition(Position position) {
        if (position.isBiggerThan(nodes.length - 1)) { // 위치값과 실제 인덱스값 동일시
            throw new IllegalArgumentException(ExceptionMessage.INVALID_POSITION.getMessage());
        }
    }

    // 선그리기 유효성 검사 통합
    private void validateDrawLinePosition(Position startPosition) {
        if (isInvalidPosition(startPosition) || isLineAtPosition(startPosition) || isLineAtNextPosition(startPosition)) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_DRAW_POSITION.getMessage());
        }
    }

    // 숫자가 사다리 크기보다 커서 실패하는 경우
    private boolean isInvalidPosition(Position startPosition) {
        return startPosition.isBiggerThan(nodes.length - 1); // startPosition.getPosition() < 0 -> position 만들때 이미 검증했던 logic
    }

    // 이미 그려진 경우
    private boolean isLineAtPosition(Position startPosition) {
        return ! nodes[startPosition.getPosition()].isAlreadySetDirection(); // none이 아니면
        // return nodes[startPosition.getPosition()] == RIGHT.getValue() || row[startPosition.getPosition()] == LEFT.getValue();
    }

    // 옆에 이미 그려진 경우
    private boolean isLineAtNextPosition(Position startPosition) {
        startPosition.next();
        boolean IsLineAtPosition = !nodes[startPosition.getPosition()].isAlreadySetDirection();
        startPosition.prev();
        return IsLineAtPosition;
    }
}