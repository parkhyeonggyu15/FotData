import { NavLink, Outlet } from "react-router-dom";
import { LiveTicker } from "./LiveTicker";
import "./Layout.css";

function navLinkClass({ isActive }: { isActive: boolean }) {
  return isActive ? "active" : undefined;
}

export function Layout() {
  return (
    <div>
      <LiveTicker />
      <header className="app-header">
        <nav className="app-nav">
          <NavLink to="/" end className="app-brand">
            <span className="app-brand-mark" aria-hidden="true">⚽</span>
            FotData
          </NavLink>
          <div className="app-nav-links">
            <NavLink to="/matches" className={navLinkClass}>경기 목록</NavLink>
            <NavLink to="/rankings" className={navLinkClass}>순위표</NavLink>
            <NavLink to="/h2h" className={navLinkClass}>상대전적</NavLink>
            <NavLink to="/predictions" className={navLinkClass}>예측</NavLink>
          </div>
        </nav>
      </header>
      <main className="app-main">
        <div className="page">
          <Outlet />
        </div>
      </main>
    </div>
  );
}
