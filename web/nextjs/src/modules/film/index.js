import Link from "next/link";
import fetch from "isomorphic-unfetch";

const Index = (props) => (
    <div>
        <h1>Marvel TV Shows</h1>
        <ul>
            {props.shows.map(({show}) => {
                return (
                    <li key={show.id}>
                        <Link href={`/film/detail?id=${show.id}`}>
                            <a>{show.name}</a>
                        </Link>
                    </li>
                );
            })}
        </ul>
    </div>
);

Index.getInitialProps = async function() {
    const res = await fetch("https://api.tvmaze.com/search/shows?q=marvel");
    const data = await res.json();
    return {
        shows: data
    };
};

export default Index;
