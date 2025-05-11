import { Route, Routes } from 'react-router-dom';

import './styles/main.scss'
import Login from './Pages/LoginPage/Login';
import HomePage from './Pages/HomePage/HomePage';
import NotFoundPage from './Pages/NotFoundPage/NotFoundPage';

function App() {
  return (
    <Routes>
      <Route path='/' element={<Login/>}/>
      <Route path='/Home' element={<HomePage/>}/>
      <Route path='*' element={<NotFoundPage/>}/>
    </Routes>
  );
}

export default App;
