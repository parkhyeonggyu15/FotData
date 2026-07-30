import { NavLink, Outlet } from "react-router-dom";

export function Layout() {
  return (
    <div>
      <header>
        <nav>
          <NavLink to="/matches">경기 목록</NavLink>
          <NavLink to="/rankings">순위표</NavLink>
          <NavLink to="/h2h">상대전적</NavLink>
        </nav>
      </header>
      <main>
        <Outlet />
      </main>
    </div>
  );
}
