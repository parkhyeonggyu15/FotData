# 해외축구 분석 사이트 — 프로젝트 메모리

## 프로젝트 개요
- 해외축구 경기 결과 및 분석 사이트 (포트폴리오/취업용)
- 대상 리그: UEFA 챔피언스리그, EPL, 라리가, 분데스리가, 세리에 A (1부)
- 데이터 수집: football-data.org API 기반 배치 수집 + 자체 분석 지표 설계

## 현재 상태 (2026-07-30 기준)
- Entity/Repository/DTO/Service 계층까지 구현 완료 (커밋 6c2a93d)
- 이후 작업 중, 아직 커밋 안 됨:
  - `controller/` 신규: MatchController, TeamStatsController, AnalysisController, AdminSyncController
  - `scheduler/` 신규: MatchSyncScheduler (배치 수집 스케줄링 구현)
  - `service/MatchQueryService.java`, `service/SeasonCalculator.java` 신규
  - `FotDataApplication.java`, `MatchSyncService.java`, `TeamStatsService.java`, `application.properties` 수정

## 다음에 할 일 (TODO)
- [ ] 현재 미커밋 상태(controller/scheduler 계층) 커밋
- [ ] 자체 분석 지표 항목 정의 및 AnalysisService 검증
- [ ] API 엔드포인트 테스트 (Controller 계층 동작 확인)

## 주요 결정 사항 / 왜 이렇게 했는지
- 데이터 소스: football-data.org 선택 — (이유 적어두면 나중에 유용)

## 환경 동기화
- 마지막 작업한 컴퓨터: (학원 / 집)
- 마지막 커밋 해시: 6c2a93d (브랜치: dev) — 이후 미커밋 변경사항 있음 (위 "현재 상태" 참고)
- 로컬에만 있고 git에 안 올라간 것: football-data.org API 키(.env) — 컴퓨터마다 재설정 필요
- 컴퓨터별 차이점: (있으면 기록)

## 자주 쓰는 명령어 / 환경 정보
- 빌드/실행 명령어:
- API Base URL: https://api.football-data.org/v4

## 알아둬야 할 제약사항
- football-data.org 무료 티어는 분당 요청 수 제한 있음 → 배치 수집 시 주의

## 다음 세션 시작 체크리스트
1. git pull
2. .env에 API 키 설정 확인
3. 마지막 TODO부터 이어서