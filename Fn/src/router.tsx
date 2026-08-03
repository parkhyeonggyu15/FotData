import { createBrowserRouter } from "react-router-dom";
import { Layout } from "./components/Layout";
import { HomePage } from "./features/home/HomePage";
import { MatchListPage } from "./features/matches/MatchListPage";
import { RankingsPage } from "./features/rankings/RankingsPage";
import { TeamDetailPage } from "./features/team/TeamDetailPage";
import { H2HPage } from "./features/h2h/H2HPage";
import { PredictionsPage } from "./features/predictions/PredictionsPage";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      { index: true, element: <HomePage /> },
      { path: "matches", element: <MatchListPage /> },
      { path: "rankings", element: <RankingsPage /> },
      { path: "teams/:teamId", element: <TeamDetailPage /> },
      { path: "h2h", element: <H2HPage /> },
      { path: "predictions", element: <PredictionsPage /> },
    ],
  },
]);
