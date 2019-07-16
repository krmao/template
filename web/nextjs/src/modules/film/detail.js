import fetch from "isomorphic-unfetch";

const Post = (props) => (
    <div>
        <h1>{props && props.show && props.show.name ? props.show.name : ""}</h1>
        <p>{props && props.show && props.show.summary ? props.show.summary.replace(/<[/]?p>/g, "") : ""}</p>

        {console.log("props", props)}
        {props && props.show && props.show.image != null && props.show.image.medium != null ? <img src={props.show.image.medium} alt={""} /> : null}
    </div>
);

Post.getInitialProps = async function(context) {
    const {id} = context.query;
    const res = await fetch(`https://api.tvmaze.com/shows/${id}`);
    const show = await res.json();
    return {show};
};

export default Post;
