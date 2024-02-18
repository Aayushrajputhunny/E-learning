import React from "react";
import Header from "../../Header";
import { Row } from "react-bootstrap";
import { Col } from "reactstrap";
import Example from "../../Teacher Content/Navbar";
import ReactPlayer from "react-player";
import Footer from "../../components/Footer";
import { Button } from 'reactstrap';
import { useNavigate } from 'react-router-dom'
import { useState } from "react";
import { useEffect } from "react";
import axios from "axios";
import { Sidebar, Menu, MenuItem, SubMenu } from 'react-pro-sidebar';
import { Link } from "react-router-dom";
const StudentContent = () => {


  const [Courses, setCourses] = useState([]);
  const [refValue, setRefValue] = useState(""); //email
  const [courseName, setcoursename] = useState(""); 

  const email = sessionStorage["email"]
  useEffect(() => {
    axios.get("http://localhost:8080/home/" + email).then((Response) => {setRefValue(Response.data)})
    }, [email])

    const courseid = sessionStorage["Course"]
    useEffect(() => {
      axios.get("http://localhost:8080/teacher/Course/name/" + courseid).then((Response) => {setcoursename(Response.data.message)})
      }, [courseName, courseid])
 
   
  const navigate = useNavigate() 
  const OnLogOut = () => {
    sessionStorage.removeItem('Authenticate')
    navigate('/AddContent')
}

const imp = sessionStorage["Course"]
useEffect(() => {
  axios.get(`http://localhost:8080/teacher/course_content/${imp}`).then((Response) => {setCourses(Response.data)});
}, [imp]);

const [luffy, setluffy] = useState({title : " ", description : " ", filePath : " "});

const dosomething = (bottle) => {
setluffy({title : bottle.title, description : bottle.description, filePath : bottle.filePath})
}



console.log(luffy)

    return(
 
        <div>
         
            <Example/>
            <br/>
            <Row>
            <Col md={3}>
            {Courses.length > 0 ? Courses.map((item) => (
            <div key={item.id}>
           <Sidebar>
      <Menu>
        {/* <MenuItem> {item.title} </MenuItem> */}
              <table>
                <tr>
                  <td style={{ width: '150px' }}>
                  <Link className="nav-link" onClick={() => dosomething(item)} key={item.id}> {item.title} </Link>
                  </td>
                </tr>
              </table>
    
    
        {/* <div><Link className="nav-link" onClick={() => dosomething(item)} key={item.id}> {item.title} </Link> <Button outline color="danger" onClick={() => deletesomething(item.id)} size="sm" className="ml-4">delete</Button></div> */}
      </Menu>
    </Sidebar>
            </div>
          )) : "No Contents"}
          </Col>
    
          <Col md={9}>
          <Header name = {refValue.name} contents = {courseName}/>
          <br/>
          <Row>
            <Col md={5}>
        <h4>This is Content for {luffy.title}</h4>
        <br/>
        <p><h6>{luffy.description}</h6></p>
          <br/>
            </Col>
            <Col md={7}>
           <ReactPlayer url = {luffy.filePath}
            controls={true}/>
            </Col>
          </Row>
         
          </Col>
            </Row>
            <br/>
            <div className="text-center"> 
    <Button color="primary" size="lg" active>Previous</Button>{' '}
    <Button color="primary" size="lg" active className="ml-3">Next</Button>
</div>
            <Footer/>
        </div>
    );
}

export default StudentContent;