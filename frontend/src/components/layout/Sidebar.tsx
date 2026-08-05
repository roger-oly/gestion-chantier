import { Link } from "react-router-dom";


export default function Sidebar() {

  return (
    <aside>

      <nav>

        <ul>

          <li>
            <Link to="/dashboard">
              Dashboard
            </Link>
          </li>


          <li>
            <Link to="/chantiers">
              Chantiers
            </Link>
          </li>


          <li>
            <Link to="/utilisateurs">
              Utilisateurs
            </Link>
          </li>


        </ul>

      </nav>

    </aside>
  );
}